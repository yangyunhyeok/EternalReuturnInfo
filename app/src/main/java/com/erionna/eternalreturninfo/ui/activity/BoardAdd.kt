package com.erionna.eternalreturninfo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.erionna.eternalreturninfo.databinding.BoardAddActivityBinding
import com.erionna.eternalreturninfo.model.BoardModel
import com.erionna.eternalreturninfo.model.CommentModel
import com.erionna.eternalreturninfo.retrofit.FBRef
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

            // 수정 : 로그인한 사용자 닉네임 가져오기!
            val title = boardAddEtTitle.text.toString()
            val content = boardAddEtContent.text.toString()
            val date = getTime()

            val key = FBRef.postRef.push().key.toString()

            FBRef.postRef.child(key).setValue(BoardModel(key, title, content, "user1", date, mutableListOf())).addOnSuccessListener {
                Toast.makeText(this@BoardAdd, "게시글 추가!", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { e ->
                Toast.makeText(this@BoardAdd, "게시글 추가 실패!" + e.message, Toast.LENGTH_SHORT).show()
            }

            finish()
        }

    }

    fun getTime(): String {
        val currentDateTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA).format(currentDateTime)

        return dateFormat
    }
}