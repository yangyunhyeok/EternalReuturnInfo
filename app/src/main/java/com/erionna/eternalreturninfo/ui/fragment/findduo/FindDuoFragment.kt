package com.erionna.eternalreturninfo.ui.fragment.findduo

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.erionna.eternalreturninfo.R
import com.erionna.eternalreturninfo.databinding.FindDuoFragmentBinding
import com.erionna.eternalreturninfo.model.ERModel
import com.erionna.eternalreturninfo.model.User
import com.erionna.eternalreturninfo.ui.activity.BoardDialog
import com.erionna.eternalreturninfo.ui.activity.ChatActivity
import com.erionna.eternalreturninfo.ui.activity.DialogListener
import com.erionna.eternalreturninfo.ui.activity.MainActivity
import com.erionna.eternalreturninfo.ui.fragment.signin.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class FindDuoFragment : Fragment() {
    companion object {
        fun newInstance() = FindDuoFragment()

    }

    private var _binding: FindDuoFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var mDbRef: DatabaseReference

    private lateinit var mAuth: FirebaseAuth

    private var mUID = ""

    //파이어스터오랑 연동하기위한 코드

    private val firestore = FirebaseFirestore.getInstance()

    //팝업창이랑 연결

    private lateinit var findduoPopupWindow: FindduoPopupWindow


    private val adapter: FindduoAdapter by lazy {
        FindduoAdapter(
            requireContext(),
            onClickUser = { position, item ->
                Log.d("choco5733", "$item")
                if (item.uid != mAuth.uid) {
                    val customDialog = BoardDialog(requireContext(), item.name ?: "",object :
                        DialogListener {
                        override fun onOKButtonClicked() {
                            startActivity(
                                ChatActivity.newIntent(
                                    requireContext(),
                                    item
                                )
                            )
                        }
                    })
                    customDialog.show()
                } else {
                    val mainActivity = activity as MainActivity
                    mainActivity.binding.tabLayout.getTabAt(4)?.select()
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FindDuoFragmentBinding.inflate(inflater, container, false)

        mAuth = FirebaseAuth.getInstance()
        // db 초기화
        mDbRef = FirebaseDatabase.getInstance().reference
        // mUID 초기화
        mUID = mAuth.currentUser?.uid ?: ""

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.findduoRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.findduoRecyclerview.adapter = adapter

        initView()

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initView() = with(binding) {

        findduoPopupWindow = FindduoPopupWindow(requireContext())

        binding.findduoRegisterBtn.setOnClickListener { findduoPopupWindow.showPopup(binding.root) }

        adapter.notifyDataSetChanged()

        loadAllUserDataFromFirebase()
    }

    private fun loadAllUserDataFromFirebase() {
        // Firebase Realtime Database 경로
        val databasePath = "user"

        // 데이터베이스에서 모든 사용자 정보 가져오기
        mDbRef.child(databasePath).orderByChild("timestamp").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val filteredUsersList = ArrayList<ERModel>() // 필터링된 사용자 목록

                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(ERModel::class.java)

                        // 유저의 필드 중 하나라도 null이 아니면 필터링 대상에 포함
                        if (user != null &&
                            !user.server.isNullOrEmpty() &&
                            !user.name.isNullOrEmpty() &&
                            !user.gender.isNullOrEmpty() &&
                            !user.tier.isNullOrEmpty()
                        ) {
                            filteredUsersList.add(user)
                        }
                    }

                    // RecyclerView 어댑터의 데이터 소스에 필터링된 사용자 정보 추가
                    adapter.items.clear()
                    adapter.items.addAll(filteredUsersList)
                    // RecyclerView 갱신
                    adapter.notifyDataSetChanged()

                    val filteredUserCount = filteredUsersList.size
                    binding.findduoTotalNumber.text = filteredUserCount.toString() // 필터링된 사용자 수 표시
                } else {
                    // 데이터가 존재하지 않는 경우
                    Log.d(TAG, "No user data found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // 데이터 가져오기가 실패한 경우
                Log.e(TAG, "Data retrieval failed: $error")
            }
        })
    }


}