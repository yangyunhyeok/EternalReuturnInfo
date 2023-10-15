package com.erionna.eternalreturninfo.ui.fragment

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.erionna.eternalreturninfo.R
import com.erionna.eternalreturninfo.databinding.ChatListFragmentBinding
import com.erionna.eternalreturninfo.model.ERModel
import com.erionna.eternalreturninfo.model.User
import com.erionna.eternalreturninfo.ui.activity.ChatActivity
import com.erionna.eternalreturninfo.ui.adapter.ChatListAdapter
import com.erionna.eternalreturninfo.ui.viewmodel.ChatListViewModel
import com.erionna.eternalreturninfo.ui.viewmodel.ChatListViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Random

class ChatListFragment : Fragment() {

    companion object {
        fun newInstance() = ChatListFragment()
    }

    private var _binding: ChatListFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private val chatListAdapter by lazy {
        ChatListAdapter(
            onClickItem = { position, item ->
                val intent = Intent(activity, ChatActivity::class.java)
                intent.putExtra("testParse",item)
                startActivity(intent)

            }
        )
    }

    private val viewModel: ChatListViewModel by lazy {
        ViewModelProvider(this, ChatListViewModelFactory())[ChatListViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChatListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initModel()
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    private fun initView() = with(binding) {
        chatListRecyclerview.adapter = chatListAdapter
        chatListRecyclerview.layoutManager = LinearLayoutManager(context)

        // 인증 초기화
        auth = Firebase.auth
        // 데이터베이스 초기화
        database = Firebase.database.reference

        chatListToolbar.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {

                R.id.sign_up -> {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("회원가입")
                    builder.setIcon(R.mipmap.ic_launcher)

                    val v1 = layoutInflater.inflate(R.layout.dialog_sign_up, null)
                    builder.setView(v1)

                    val listener = DialogInterface.OnClickListener { p0, p1 ->
                        val alert = p0 as AlertDialog
                        val edit1: EditText? = alert.findViewById<EditText>(R.id.dialog_email)
                        val edit2: EditText? = alert.findViewById<EditText>(R.id.dialog_name)
                        val edit3: EditText? = alert.findViewById<EditText>(R.id.dialog_pwd)

                        val email = edit1?.text.toString()
                        val name = edit2?.text.toString()
                        val password = edit3?.text.toString()

                        signUp(email, name, password)
                    }

                    builder.setPositiveButton("확인", listener)
                    builder.setNegativeButton("취소", null)

                    builder.show()
                    true
                }

                R.id.sign_in -> {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("로그인")
                    builder.setIcon(R.mipmap.ic_launcher)

                    val v1 = layoutInflater.inflate(R.layout.dialog_sign_in, null)
                    builder.setView(v1)

                    val listener = DialogInterface.OnClickListener { p0, p1 ->
                        val alert = p0 as AlertDialog
                        val edit1: EditText? = alert.findViewById<EditText>(R.id.dialog_email)
                        val edit3: EditText? = alert.findViewById<EditText>(R.id.dialog_pwd)

                        val email = edit1?.text.toString()
                        val password = edit3?.text.toString()

                        signIn(email, password)
                    }

                    builder.setPositiveButton("확인", listener)
                    builder.setNegativeButton("취소", null)

                    builder.show()
                    true
                }

                else -> {
                    auth.signOut()
                    Toast.makeText(requireContext(), "로그아웃", Toast.LENGTH_SHORT).show()
                    viewModel.clearList()
                    binding.chatListTitle.text = "채팅"
                    true
                }
            }
        }

    }

    // 회원가입
    private fun signUp(email: String, name: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(requireContext(), "회원가입 성공", Toast.LENGTH_SHORT).show()
                    addUserToDatabase(email, name, password, auth.currentUser!!.uid)
//                    auth.signOut()
                    viewModel.clearList()
//                    viewModel.deleteUser(name)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(requireContext(), "회원가입 실패", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // 로그인
    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    viewModel.clearList() // 새로 로그인 했을 시 기존 리스트 초기화
                    Toast.makeText(requireContext(), "로그인 성공", Toast.LENGTH_SHORT).show()

                    // 회원 정보 가져오기
                    database.child("user").addValueEventListener(object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            // 가져오기 성공 시
                            for(postSnapshot in snapshot.children) {
                                val currentUser = postSnapshot.getValue(ERModel::class.java)
                                if(auth.currentUser?.uid != currentUser?.uid) {
                                    viewModel.addUser(currentUser?.copy(msg = "서버로부터 회원정보 불러오기 성공!"))
                                } else {
                                    binding.chatListTitle.text = "채팅" + " 접속자 : (${currentUser?.name}) "
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // 가져오기 실패 시
                            Toast.makeText(requireContext(),"가져오기 실패",Toast.LENGTH_SHORT).show()
                        }

                    })

                } else {
                    Toast.makeText(requireContext(), "비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show()
                    Log.d("#choco5732", "${task.exception}")
                }
            }
    }

    // 데이터베이스에 유저정보 저장
    private fun addUserToDatabase(email: String, name: String, password: String, uId: String) {
        // 랜덤 이미지
        val imageResources = arrayOf(R.drawable.ic_alonso, R.drawable.ic_aya, R.drawable.ic_daniel, R.drawable.ic_felix, R.drawable.ic_mai)
        val random = Random()
        val randomIndex = random.nextInt(imageResources.size)
        val randomImageResource = imageResources[randomIndex]
        Log.d("#choco5732", "$randomImageResource")

        database.child("user").child(uId)
            .setValue(ERModel(profilePicture = randomImageResource, email = email, password = password, name = name, uid = uId))
    }

    // 뷰모델
    private fun initModel() = with(viewModel) {
        list.observe(viewLifecycleOwner) {
            chatListAdapter.submitList(it)
        }
    }
}