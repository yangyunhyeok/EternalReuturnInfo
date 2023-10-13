package com.erionna.eternalreturninfo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.erionna.eternalreturninfo.databinding.BoardPostActivityBinding
import com.erionna.eternalreturninfo.model.BoardModel
import com.erionna.eternalreturninfo.model.Comment
import com.erionna.eternalreturninfo.ui.adapter.BoardCommentRecyclerViewAdpater

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

    private fun initView() = with(binding) {

        boardPostRvComment.adapter = listAdapter
        boardPostRvComment.layoutManager = LinearLayoutManager(this@BoardPost)

        val list = mutableListOf<BoardModel>()
        list.add((BoardModel("[일반]   캐릭터어쩌구저쩌구", "캐릭터는 이게 짱짱!", "ER 짱!", "2023.10.11", Comment("ER 짱", "넵! 잘 지키겠습니다.", "2023.10.12"), 0)))
        list.add((BoardModel("[일반]   캐릭터어쩌구저쩌구", "캐릭터는 이게 짱짱!", "ER 짱!", "2023.10.11", Comment("ER 짱", "넵! 잘 지키겠습니다.", "2023.10.12"), 0)))
        listAdapter.submitList(list)

        boardPostBtnSave.setOnClickListener {
            list.add((BoardModel("[일반]   캐릭터어쩌구저쩌구", "캐릭터는 이게 짱짱!", "ER 짱!", "2023.10.11", Comment("ER 짱", "넵! 잘 지키겠습니다.", "2023.10.12"), 0)))
            listAdapter.submitList(list)
            listAdapter.notifyDataSetChanged()
        }

        boardPostIbBack.setOnClickListener {
            finish()
        }

    }

}