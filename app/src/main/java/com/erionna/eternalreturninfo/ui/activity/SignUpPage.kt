package com.erionna.eternalreturninfo.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Nickname
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.erionna.eternalreturninfo.R
import com.erionna.eternalreturninfo.databinding.SignupInformationActivityBinding
import com.erionna.eternalreturninfo.model.SignUpData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firestore.v1.FirestoreGrpc.FirestoreImplBase

class SignUpPage : AppCompatActivity() {
    private lateinit var binding: SignupInformationActivityBinding
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupInformationActivityBinding.inflate(layoutInflater)
        auth = Firebase.auth
        setContentView(binding.root)

        val characterlist = resources.getStringArray(R.array.character)
        val adapter = ArrayAdapter<String>(this, R.layout.signup_spinner,R.id.spinner_tv,characterlist)
        var selectCharacter = characterlist[0]
        binding.signupCharacterSp.adapter = adapter

        binding.signupCharacterSp.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectCharacter = characterlist[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        binding.signupSignupBtn.setOnClickListener {
            createAccount(
                binding.signupIDEt.text.toString(),
                binding.signupPWEt.text.toString(),
                binding.signupPWCheckEt.text.toString(),
                binding.signupNickNameEt.text.toString(),
                selectCharacter
            )
        }


    }


    private fun createAccount(email: String, password: String, passwordCheck: String,nickname: String,character:String) {

        if (email.isNotEmpty() && password.isNotEmpty() && passwordCheck.isNotEmpty()) {
            if (password == passwordCheck) {
                auth?.createUserWithEmailAndPassword(email, password)
                    ?.addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this, "계정 생성 완료.",
                                Toast.LENGTH_SHORT
                            ).show()
                            setDocument(
                                SignUpData(
                                    Email = binding.signupIDEt.text.toString(),
                                    PW = binding.signupPWEt.text.toString(),
                                    NickName = binding.signupNickNameEt.text.toString(),
                                    Character = character,
                                    profile = "미구현"
                                )
                            )
                            Toast.makeText(this, "$character", Toast.LENGTH_SHORT).show()
                            finish()
                            var intent = Intent(this,MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this, "계정 생성 실패",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this, "같은 비밀번호를 입력하세요.ㅅ", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "회원가입 정보를 입력하세요", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setDocument(data: SignUpData) {
        FirebaseFirestore.getInstance()
            .collection("EternalReturnInfo")
            .document(auth.uid!!)
            .set(data)
            .addOnSuccessListener {
                Toast.makeText(this, "닉네임 값 저장 성공", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "닉네임 값 저장 실패", Toast.LENGTH_SHORT).show()
            }
    }



}

