package com.erionna.eternalreturninfo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler.Value
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.erionna.eternalreturninfo.databinding.BoardPostActivityBinding
import com.erionna.eternalreturninfo.model.BoardModel
import com.erionna.eternalreturninfo.model.CommentModel
import com.erionna.eternalreturninfo.retrofit.FBRef
import com.erionna.eternalreturninfo.ui.adapter.BoardCommentRecyclerViewAdpater
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BoardPost : AppCompatActivity() {

    private lateinit var binding: BoardPostActivityBinding

    private val listAdapter by lazy {
        BoardCommentRecyclerViewAdpater()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BoardPostActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private val commentList = mutableListOf<CommentModel>()

    private fun initView() = with(binding) {

        boardPostRvComment.adapter = listAdapter
        boardPostRvComment.layoutManager = LinearLayoutManager(this@BoardPost)

        val id = intent.getStringExtra("ID") ?: ""

        if (id != null) {
            FBRef.postRef.child(id).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val board = snapshot.getValue<BoardModel>()

                        boardPostTvTitle.text = "[일반]  " + board?.title
                        boardPostTvContent.text = board?.content
                        boardPostTvUser.text = board?.author
                        boardPostTvDate.text = board?.date

//                        if(board?.comment?.size == 0){
//                            boardPostBtnComment.visibility = View.INVISIBLE
//                        }else{
//                            boardPostBtnComment.text = board?.comment?.size.toString()
//
//                            val comments = board?.comment
//                            Log.d("comments", comments.toString())
//                            listAdapter.submitList(comments)
//                            listAdapter.notifyDataSetChanged()
//                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }

        boardPostBtnSave.setOnClickListener {

            val content = boardPostEtComment.text.toString()
            val date = getTime()

//            val newComment = CommentModel("user1", content, date)
//
//            val commentkey = FBRef.postRef.child(id).child("comment").push().key.toString()
//
//            FBRef.postRef.child(id).child("comment").child(commentkey).setValue(newComment)
//                .addOnSuccessListener {
//                    // 성공적으로 댓글이 추가된 경우 처리할 내용
//                    Toast.makeText(this@BoardPost, "댓글이 추가되었습니다.", Toast.LENGTH_SHORT).show()
//                }
//                .addOnFailureListener {
//                    // 댓글 추가에 실패한 경우 처리할 내용
//                    Toast.makeText(this@BoardPost, "댓글 추가에 실패했습니다.", Toast.LENGTH_SHORT).show()
//                }

//            //로그인한 사용자 닉네임 불러오기
//            commentList.add(CommentModel("user1", content, date))


        }

        boardPostIbBack.setOnClickListener {
            finish()
        }

    }

    fun getTime(): String {
        val currentDateTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA).format(currentDateTime)

        return dateFormat
    }

}