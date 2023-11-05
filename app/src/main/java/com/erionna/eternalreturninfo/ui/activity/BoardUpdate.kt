package com.erionna.eternalreturninfo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.erionna.eternalreturninfo.databinding.BoardAddActivity2Binding
import com.erionna.eternalreturninfo.databinding.BoardAddActivityBinding
import com.erionna.eternalreturninfo.model.BoardModel
import com.erionna.eternalreturninfo.retrofit.FBRef

class BoardUpdate : AppCompatActivity() {

    private lateinit var binding: BoardAddActivity2Binding
    private var category = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BoardAddActivity2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {

        val board = intent.getParcelableExtra<BoardModel>("updateBoard")

        if (board != null) {
            boardAddEtTitle.setText(board.title)
            boardAddEtContent.setText(board.content)

            when(board.category){
                "공지" -> {
                    boardAddSpinner.selectItemByIndex(0)
                    category = "공지"
                }
                "일상" -> {
                    boardAddSpinner.selectItemByIndex(1)
                    category = "일상"
                }
                "질문" -> {
                    boardAddSpinner.selectItemByIndex(2)
                    category = "질문"
                }
                "공략" -> {
                    boardAddSpinner.selectItemByIndex(3)
                    category = "공략"
                }
            }
        }

        boardAddSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->

            when(newText){
                "공지" -> category = "공지"
                "일상" -> category = "일상"
                "질문" -> category = "질문"
                "공략" -> category = "공략"
            }
        }

        boardAddIbBack.setOnClickListener {
            finish()
        }

        boardAddBtnFinish.setOnClickListener {

            val category = category
            val title = boardAddEtTitle.text.toString()
            val content = boardAddEtContent.text.toString()

            val updateBoard = board?.let { it1 -> BoardModel(it1.id, category, title, content, board.author, board.date, board.comments, board.views) }

            if (board != null) {
                FBRef.postRef.child(board.id).setValue(updateBoard)
            }

            finish()

        }

    }

}