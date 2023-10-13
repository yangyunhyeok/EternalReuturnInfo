package com.erionna.eternalreturninfo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.erionna.eternalreturninfo.databinding.BoardAddActivityBinding

class BoardAdd : AppCompatActivity() {

    private lateinit var binding: BoardAddActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BoardAddActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {

        boardAddIbBack.setOnClickListener {
            finish()
        }

        boardAddBtnFinish.setOnClickListener {
            finish()
        }

    }
}