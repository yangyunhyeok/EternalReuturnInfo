package com.erionna.eternalreturninfo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.erionna.eternalreturninfo.databinding.BoardPostActivityBinding
import com.erionna.eternalreturninfo.model.BoardModel
import com.erionna.eternalreturninfo.model.CommentModel
import com.erionna.eternalreturninfo.retrofit.FBRef
import com.erionna.eternalreturninfo.ui.adapter.BoardCommentRecyclerViewAdpater
import com.erionna.eternalreturninfo.ui.viewmodel.BoardListViewModel
import com.erionna.eternalreturninfo.ui.viewmodel.BoardListViewModelFactory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class BoardPost : AppCompatActivity() {

    private lateinit var binding: BoardPostActivityBinding

    private val listAdapter by lazy {
        BoardCommentRecyclerViewAdpater()
    }

    private val boardViewModel by lazy {
        ViewModelProvider(this, BoardListViewModelFactory()).get(BoardListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BoardPostActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initModel()
    }

    private fun initView() = with(binding) {

        boardPostRvComment.adapter = listAdapter
        boardPostRvComment.layoutManager = LinearLayoutManager(this@BoardPost)

        val id = intent.getStringExtra("ID") ?: ""

        FBRef.postRef.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    val board = snapshot.getValue<BoardModel>()

                    boardPostTvTitle.text = "[일반]  " + board?.title
                    boardPostTvContent.text = board?.content
                    boardPostTvUser.text = board?.author

                    if (board != null) {
                        boardPostTvDate.text =
                            board?.date?.let { formatTimeOrDate(it) }
                    }

                    if(board?.comments?.size == 0){
                        boardPostBtnComment.visibility = View.INVISIBLE
                    }else{
                        boardPostBtnComment.text = board?.comments?.size.toString()

                        val comments = board?.comments?.values?.toList()
                        val sortedComment = comments?.sortedBy { Date(it.date) }
                        if (sortedComment != null) {
                            boardViewModel.initComment(sortedComment.toMutableList())
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        boardPostBtnSave.setOnClickListener {

            val content = boardPostEtComment.text.toString()
            val date = Calendar.getInstance().timeInMillis

            val commentkey = FBRef.postRef.child(id).child("comments").push().key.toString()

            //로그인한 사용자 닉네임 불러오기
            val newComment = CommentModel(commentkey, "user2", content, date)

            FBRef.postRef.child(id).child("comments").child(commentkey).setValue(newComment)
            boardViewModel.addComment(newComment)

            boardPostEtComment.setText("")
        }

        boardPostIbBack.setOnClickListener {
            finish()
        }

    }

    private fun initModel() = with(binding) {
        boardViewModel.commentList.observe(this@BoardPost){ commentList ->
            listAdapter.submitList(commentList)
        }
    }

    fun formatTimeOrDate(postTime: Long): String {

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val calendar2 = Calendar.getInstance()
        calendar2.set(Calendar.HOUR_OF_DAY, 23)
        calendar2.set(Calendar.MINUTE, 59)
        calendar2.set(Calendar.SECOND, 59)

        val date1 = calendar.time
        val date2 = calendar2.time

        if(date1 <= Date(postTime) && Date(postTime) <= date2){
            val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            return simpleDateFormat.format(Date(postTime))
        }else{
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return simpleDateFormat.format(Date(postTime))
        }

    }

}