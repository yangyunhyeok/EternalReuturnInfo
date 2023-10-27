package com.erionna.eternalreturninfo.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.erionna.eternalreturninfo.R
import com.erionna.eternalreturninfo.databinding.MyprofileFragmentBinding
import com.erionna.eternalreturninfo.model.BoardModel
import com.erionna.eternalreturninfo.retrofit.BoardSingletone
import com.erionna.eternalreturninfo.retrofit.FBRef
import com.erionna.eternalreturninfo.ui.activity.BoardDeleted
import com.erionna.eternalreturninfo.ui.activity.BoardPost
import com.erionna.eternalreturninfo.ui.adapter.BoardRecyclerViewAdapter
import com.erionna.eternalreturninfo.ui.adapter.NoticeBannerListAdapter
import com.google.android.gms.auth.api.signin.GoogleSignIn

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyProfileFragment : Fragment() {
    private val binding get() = _binding!!
    private var _binding: MyprofileFragmentBinding? = null
    private var auth: FirebaseAuth? = null
    var mGoogleSignInClient: GoogleSignInClient? = null
    var db = Firebase.firestore

    private val boardListAdapter by lazy {
        BoardRecyclerViewAdapter()
    }

    companion object {
        fun newInstance() = MyProfileFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MyprofileFragmentBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        var uid = auth?.uid.toString()
        Patch(uid)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        myprofileRvBoard.adapter = boardListAdapter
        myprofileRvBoard.layoutManager = LinearLayoutManager(requireContext())

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
                    Log.d("데이터", "${document.data}")
                } else {
                    Log.d("마이페이지", "No such document")
                }
            }
    }

    fun ImgPacth(character: String) {
        when (character) {
            "헤이즈" -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_01haze)
            "쇼우" -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_02xiukai)
            "나딘" -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_03nadine)
            "나타폰" -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_04nathapon)
            "니키" -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_05nicty)
            "다니엘" -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_06daniel)
            "띠아" -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_07tia)
            "라우라" -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_08laura)
            "레녹스" -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_09lenox)
            "레온" -> binding.myprofileCharacterImg.setImageResource(R.drawable.ic_character_10leon)
        }
    }

    fun GooglePatch(){

    }


}