package com.erionna.eternalreturninfo.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.erionna.eternalreturninfo.R
import com.erionna.eternalreturninfo.databinding.MyprofileCharacterDialogBinding
import com.erionna.eternalreturninfo.databinding.MyprofileFragmentBinding
import com.erionna.eternalreturninfo.model.BoardModel
import com.erionna.eternalreturninfo.retrofit.BoardSingletone
import com.erionna.eternalreturninfo.retrofit.FBRef
import com.erionna.eternalreturninfo.ui.activity.BoardDeleted
import com.erionna.eternalreturninfo.ui.activity.BoardPost
import com.erionna.eternalreturninfo.ui.activity.LoginPage
import com.erionna.eternalreturninfo.ui.adapter.BoardRecyclerViewAdapter
import com.erionna.eternalreturninfo.ui.adapter.VideoListAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MyProfileFragment : Fragment() {
    private val binding get() = _binding!!
    private var _binding: MyprofileFragmentBinding? = null
    private var auth: FirebaseAuth? = null
    var db = Firebase.firestore
    private lateinit var characterbinding: MyprofileCharacterDialogBinding
    private val PICK_IMAGE = 1111
    val storage = Firebase.storage
    var email: String? = null

    companion object {
        fun newInstance() = MyProfileFragment()
    }

    private val boardListAdapter by lazy {
        BoardRecyclerViewAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MyprofileFragmentBinding.inflate(inflater, container, false)
        characterbinding = MyprofileCharacterDialogBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        var uid = auth?.uid.toString()
        Patch(uid)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()

        binding.myprofileMyboardRv.adapter = boardListAdapter
        binding.myprofileMyboardRv.layoutManager = LinearLayoutManager(requireContext())

        val query = FBRef.postRef.orderByChild("author").equalTo(BoardSingletone.LoginUser().uid)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if(dataSnapshot.exists()){

                    val boardList = mutableListOf<BoardModel>()

                    for (snapshot in dataSnapshot.children) {
                        val searchBoard = snapshot.getValue<BoardModel>()
                        if (searchBoard != null) {
                            boardList.add(searchBoard)
                        }
                    }

                    boardListAdapter.submitList(boardList)
                    boardListAdapter.notifyDataSetChanged()

                }else{
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 검색이 실패한 경우
            }
        })

        boardListAdapter.setOnItemClickListener(object : BoardRecyclerViewAdapter.OnItemClickListener{
            override fun onItemClick(boardItem: BoardModel) {

                FBRef.postRef.child(boardItem.id).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            val intent = Intent(requireContext(), BoardDeleted::class.java)
                            startActivity(intent)
                        } else {
                            val views = boardItem.views + 1
                            FBRef.postRef.child(boardItem.id).child("views").setValue(views)
                            val intent = Intent(requireContext(), BoardPost::class.java)
                            intent.putExtra("ID", boardItem.id)
                            startActivity(intent)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // 데이터 읽기 실패 처리
                    }
                })

            }
        })

    }

    private fun setOnClickListener() {
        var logoutBtn = binding.myprofileLogoutBtn
        var characterBtn = binding.myprofileCharacterImg
        var profileBtn = binding.myprofileProfileImg
        profileBtn.setOnClickListener {
            selectProfile()
        }
        logoutBtn.setOnClickListener {
            Firebase.auth.signOut()
            var intent = Intent(activity, LoginPage::class.java)
            startActivity(intent)
            requireActivity().finish()
        }


        //실험체 이미지 선택
        characterBtn.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.myprofile_character_dialog, null)
            val alertDialog = AlertDialog.Builder(requireActivity())
                .setView(dialogView)
                .create()
            val characterSpinner = dialogView.findViewById<Spinner>(R.id.myprofile_character_sp)
            val button = dialogView.findViewById<Button>(R.id.myprofile_select_btn)

            val characterlist = resources.getStringArray(R.array.character)
            val adapter = ArrayAdapter<String>(
                requireContext(),
                R.layout.signup_spinner,
                R.id.spinner_tv,
                characterlist
            )
            var selectCharacter = characterlist[0]
            characterSpinner.adapter = adapter

            characterSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        selectCharacter = characterlist[position]
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
            // 실험체 선택버튼
            button.setOnClickListener {
                FirebaseFirestore.getInstance()
                    .collection("EternalReturnInfo")
                    .document(auth!!.uid!!)
                    .update("character", selectCharacter)
                    .addOnSuccessListener {
                        Log.d("실험체", "성공")
                        var uid = auth?.uid.toString()
                        Patch(uid)
                    }
                    .addOnFailureListener {
                        Log.d("실험체", "실패")
                    }
                alertDialog.dismiss()
            }
            alertDialog.show()
        }
    }

    // 마이페이지 생성
    fun Patch(uid: String) {
        val docRef = db.collection("EternalReturnInfo").document("$uid")

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    var uri = Uri.parse(document["profile"].toString())
                    binding.myprofileEmailTv.text = document["email"].toString()
                    binding.myprofileNicknameTv.text = document["nickName"].toString()
                    binding.myprofileMycharacterTv.text = document["character"].toString()
                    Glide.with(this).load(uri).into(binding.myprofileProfileImg);
                    ImgPacth(document["character"].toString())
                    email = document["email"].toString()
                }
            }
    }

    // 이미지 패치
    fun ImgPacth(character: String) {
        val array: Array<String> = resources.getStringArray(R.array.character)
        when (character) {
            array[0] -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_01haze)
            array[1] -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_02xiukai)
            array[2] -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_03nadine)
            array[3] -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_04nathapon)
            array[4] -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_05nicty)
            array[5] -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_06daniel)
            array[6] -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_07tia)
            array[7] -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_08laura)
            array[8] -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_09lenox)
            array[9] -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_10leon)
        }
    }

    override fun onResume() {
        super.onResume()
        var uid = auth?.uid.toString()
        Patch(uid)
    }

    fun selectProfile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
        startActivityForResult(intent, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE) {
            val uri: Uri? = data?.data
            if (uri != null) {
                upload(uri, email!!)
                var uid = auth?.uid.toString()
                Patch(uid)
            }
        }
    }

    fun upload(
        uri: Uri,
        email: String,
    ) {
        val storageRef = storage.reference
        val fileName = email + ".jpg"
        val riversRef = storageRef.child("/$fileName")

        riversRef.putFile(uri)
            .addOnProgressListener { taskSnapshot ->
                riversRef.downloadUrl.addOnSuccessListener { uri ->
                    FirebaseFirestore.getInstance()
                        .collection("EternalReturnInfo")
                        .document(auth!!.uid!!)
                        .update("profile",uri.toString())
                }
            }
            .addOnFailureListener { Log.i("업로드 실패", "") }
            .addOnSuccessListener { Log.i("업로드 성공", "") }
    }


    fun GooglePatch() {

    }

    fun updateCharacter(character: String) {
        ImgPacth(character)
    }


}