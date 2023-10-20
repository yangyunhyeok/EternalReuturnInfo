package com.erionna.eternalreturninfo.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.erionna.eternalreturninfo.databinding.BoardAddActivityBinding
import com.erionna.eternalreturninfo.model.BoardModel
import com.erionna.eternalreturninfo.retrofit.BoardSingletone
import com.erionna.eternalreturninfo.retrofit.FBRef
import java.util.Calendar

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
            val date = Calendar.getInstance().timeInMillis

            if(title.isEmpty()){
                Toast.makeText(this@BoardAdd, "제목을 입력해주세요!", Toast.LENGTH_SHORT).show()
            }else if(content.isEmpty()) {
                Toast.makeText(this@BoardAdd, "내용을 입력해주세요!", Toast.LENGTH_SHORT).show()
            }else if(title.isNotEmpty() && content.isNotEmpty()){

                val key = FBRef.postRef.push().key.toString()

                //로그인한 유저 UserModel 정보 가져오기!
                val newBoard = BoardModel(key, title, content, BoardSingletone.LoginUser(), date, mapOf(), 0)

                FBRef.postRef.child(key).setValue(newBoard).addOnSuccessListener {
                    Toast.makeText(this@BoardAdd, "게시글 추가!", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener { e ->
                    Toast.makeText(this@BoardAdd, "게시글 추가 실패!" + e.message, Toast.LENGTH_SHORT).show()
                }

                intent.putExtra("board", newBoard)
                setResult(RESULT_OK, intent)

                finish()
            }
        }

    }

}
