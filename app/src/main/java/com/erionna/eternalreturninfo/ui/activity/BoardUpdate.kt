package com.erionna.eternalreturninfo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.erionna.eternalreturninfo.databinding.BoardAddActivityBinding
import com.erionna.eternalreturninfo.model.BoardModel
import com.erionna.eternalreturninfo.retrofit.FBRef

class BoardUpdate : AppCompatActivity() {

    private lateinit var binding: BoardAddActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BoardAddActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {

        val board = intent.getParcelableExtra<BoardModel>("updateBoard")

        if (board != null) {
            boardAddEtTitle.setText(board.title)
            boardAddEtContent.setText(board.content)
        }

        boardAddIbBack.setOnClickListener {
            finish()
        }

        boardAddBtnFinish.setOnClickListener {

            val title = boardAddEtTitle.text.toString()
            val content = boardAddEtContent.text.toString()

            val updateBoard = board?.let { it1 -> BoardModel(it1.id, title, content, board.author, board.date, board.comments, board.views) }

            if (board != null) {
                FBRef.postRef.child(board.id).setValue(updateBoard)
            }

            finish()

        }

    }

}