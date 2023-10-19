package com.erionna.eternalreturninfo.ui.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import com.erionna.eternalreturninfo.R
import com.erionna.eternalreturninfo.databinding.BoardPostActivityBinding
import com.erionna.eternalreturninfo.model.BoardModel
import com.erionna.eternalreturninfo.model.CommentModel
import com.erionna.eternalreturninfo.retrofit.BoardSingletone
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

    private var board: BoardModel? = null
    private var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BoardPostActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initDataload()

        initView()
        initModel()
    }

    private fun initDataload() = with(binding){

        boardPostRvComment.adapter = listAdapter
        boardPostRvComment.layoutManager = LinearLayoutManager(this@BoardPost)

        id = intent.getStringExtra("ID") ?: ""

        FBRef.postRef.child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.exists()){
                    board = snapshot.getValue<BoardModel>()

                    boardPostTvTitle.text = "[일반]  " + board?.title
                    boardPostTvContent.text = board?.content
                    boardPostTvUser.text = board?.author?.user
                    boardPostTvVisit.text = board?.views.toString()

                    if(board?.author?.userImage?.isEmpty() == true){
                        boardPostIbProfile.setImageResource(R.drawable.ic_xiuk)
                    }else{
                        boardPostIbProfile.load(board?.author?.userImage)
                    }

                    //수정 : 로그인 한 사용자 일 때
                    if(board?.author?.user == BoardSingletone.LoginUser().user){
                        boardPostIbMenu.visibility = View.VISIBLE

                        boardPostIbMenu.setOnClickListener {
                            val popup = PopupMenu(binding.root.context, boardPostIbMenu) // View 변경
                            popup.menuInflater.inflate(R.menu.menu_option_comment, popup.menu)
                            popup.setOnMenuItemClickListener { menu ->
                                when (menu.itemId) {
                                    R.id.menu_comment_update -> {
                                        val updateIntent = Intent(this@BoardPost, BoardUpdate::class.java)
                                        updateIntent.putExtra("updateBoard", board)
                                        startActivity(updateIntent)
                                    }
                                    R.id.menu_comment_delete -> {
                                        finish()

                                        Log.d("board.id", board?.id.toString())
                                        FBRef.postRef.child(board?.id.toString()).removeValue()
                                    }
                                }
                                false
                            }
                            popup.show()
                        }
                    }else{
                        boardPostIbMenu.visibility = View.INVISIBLE

                        boardPostIbProfile.setOnClickListener {
                            val customDialog = BoardDialog(this@BoardPost, board?.author?.user ?: "", object : DialogListener {
                                override fun onOKButtonClicked() {
                                    Toast.makeText(this@BoardPost, "채팅창 이동", Toast.LENGTH_SHORT).show()
                                }
                            })

                            customDialog.show()
                        }

                    }

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

        //수정 : 로그인한 유저 정보 가져와서 프로필 사진 띄우기
        if(BoardSingletone.LoginUser().userImage == null){
            boardPostIbProfile.setImageResource(R.drawable.ic_xiuk)
        }else{
            boardPostIbCommentProfile.load(BoardSingletone.LoginUser().userImage)
        }
    }

    private fun initView() = with(binding) {

        binding.boardPostProgressbar.visibility = View.GONE
        binding.boardPostCommentLayout.visibility = View.VISIBLE
        binding.nestedScrollView.visibility = View.VISIBLE

        listAdapter.setOnItemClickListener(object :
            BoardCommentRecyclerViewAdpater.OnItemClickListener {
            override fun onDeleteItemClick(commentItem: CommentModel, position: Int) {
                FBRef.postRef.child(id).child("comments").child(commentItem.id).removeValue()
            }

            override fun onUpdateItemClick(commentItem: CommentModel, position: Int) {
                boardPostEtComment.setText(commentItem.content)
                boardPostBtnSave.visibility = View.INVISIBLE
                boardPostBtnUpdate.visibility = View.VISIBLE

                boardPostBtnUpdate.setOnClickListener {

                    val content = boardPostEtComment.text.toString()
                    val newComment = CommentModel(commentItem.id, commentItem.author, content, Calendar.getInstance().timeInMillis)

                    FBRef.postRef.child(id).child("comments").child(commentItem.id).setValue(newComment)

                    boardPostEtComment.setText("")

                    boardPostBtnUpdate.visibility = View.INVISIBLE
                    boardPostBtnSave.visibility = View.VISIBLE
                }

            }
        })

        boardPostBtnSave.setOnClickListener {
            val content = boardPostEtComment.text.toString()
            val date = Calendar.getInstance().timeInMillis

            val commentkey = FBRef.postRef.child(id).child("comments").push().key.toString()

            //수정 : 로그인한 사용자 닉네임, 프로필 사진 불러오기
            val newComment = CommentModel(commentkey, BoardSingletone.LoginUser(), content, date)

            FBRef.postRef.child(id).child("comments").child(commentkey).setValue(newComment)

            boardPostEtComment.setText("")
        }

        boardPostIbBack.setOnClickListener {
            intent.putExtra("updateBoard", board)
            setResult(RESULT_OK, intent)
            finish()
        }

    }

    private fun initModel() = with(binding) {
        boardViewModel.commentList.observe(this@BoardPost){ commentList ->
            listAdapter.submitList(commentList)
            boardPostBtnComment.text = commentList.size.toString()
        }
    }

    fun formatTimeOrDate(postTime: Long): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val date1 = calendar.time

        val simpleDateFormat: SimpleDateFormat
        if (Date(postTime) > date1) {
            simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        } else {
            simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        }

        return simpleDateFormat.format(Date(postTime))
    }


}