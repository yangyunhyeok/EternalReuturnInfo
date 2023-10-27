package com.erionna.eternalreturninfo.ui.fragment.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.erionna.eternalreturninfo.databinding.SignUpActivity2Binding
import com.erionna.eternalreturninfo.model.User
import com.erionna.eternalreturninfo.ui.fragment.signin.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: SignUpActivity2Binding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    private var mUID = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignUpActivity2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        // db 초기화
        mDbRef = FirebaseDatabase.getInstance().reference
        // mUID 초기화
        mUID = mAuth.currentUser?.uid ?: ""

        initView()
    }

    private fun initView() = with(binding) {

        signuppageSignupBtn.setOnClickListener {

            val email = signUpEmail.text.toString().trim()
            val pw = signUpPassword.text.toString().trim()

            signup(email, pw)
        }


    }

    private fun signup(email: String, pw: String) {

        mAuth.createUserWithEmailAndPassword(email, pw)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@SignUpActivity, "회원가입 완료", Toast.LENGTH_SHORT).show()
                    val intent: Intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    Toast.makeText(this@SignUpActivity, "회원가입 실패", Toast.LENGTH_SHORT).show()
                }
            }

    }

}