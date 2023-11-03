package com.erionna.eternalreturninfo.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.erionna.eternalreturninfo.R
import com.erionna.eternalreturninfo.databinding.SignupInformationActivityBinding
import com.erionna.eternalreturninfo.model.ERModel
import com.erionna.eternalreturninfo.model.SignUpData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class SignUpPage : AppCompatActivity() {
    private lateinit var binding: SignupInformationActivityBinding
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    private lateinit var selectedImageURI: Uri
    private val PICK_IMAGE = 1111
    private lateinit var database: DatabaseReference
    val storage = Firebase.storage
    var ImageCheck = 0

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupInformationActivityBinding.inflate(layoutInflater)
        auth = Firebase.auth
        database = Firebase.database.reference
        setContentView(binding.root)

        binding.signupProfileImg.setOnClickListener {
            selectProfile()
        }


        // 실험체 스피너
        val characterlist = resources.getStringArray(R.array.character)
        val adapter =
            ArrayAdapter<String>(this, R.layout.signup_spinner, R.id.spinner_tv, characterlist)
        var selectCharacter = characterlist[0]
        binding.signupCharacterSp.adapter = adapter

        binding.signupCharacterSp.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
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
                selectCharacter,
//                selectedImageURI
            )
        }
    }


    private fun createAccount(
        email: String,
        password: String,
        passwordCheck: String,
        nickname: String,
        character: String,
//        uri: Uri
    ) {
        if (email.isNotEmpty() && password.isNotEmpty() && passwordCheck.isNotEmpty() && nickname.isNotEmpty()) {
            if (password == passwordCheck) {
                auth?.createUserWithEmailAndPassword(email, password)
                    ?.addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this, "계정 생성 완료.",
                                Toast.LENGTH_SHORT
                            ).show()
                            var baseImage = "https://firebasestorage.googleapis.com/v0/b/eternalreturninfo-4dc4b.appspot.com/o/ic_baseImage.jpg?alt=media&token=59ee3b09-5ed8-4882-8b5d-fd620d042597&_gl=1*5tr4ei*_ga*MjY4NTI2NjgxLjE2OTY5MzI3ODU.*_ga_CW55HF8NVT*MTY5ODk3Njk1NC42MC4xLjE2OTg5Nzc1MjQuNDMuMC4w"
                            setDocument(
                                SignUpData(
                                    Email = email,
                                    PW = password,
                                    NickName = nickname,
                                    Character = character,
                                    profile = baseImage
                                )
                            )
                            database.child("user").child(auth.uid!!)
                                .setValue(ERModel(profilePicture = baseImage, email = email, password = password, name = nickname, uid = auth.uid!!))
                            if(ImageCheck == 1){
                                upload(selectedImageURI, email)
                            }
                            var intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this, "계정 생성 실패",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(this, "같은 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
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
            }
            .addOnFailureListener {
                Toast.makeText(this, "닉네임 값 저장 실패", Toast.LENGTH_SHORT).show()
            }
    }

    fun selectProfile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
        startActivityForResult(intent, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE) {
            val uri: Uri? = data?.data
            if (uri != null) {
                selectedImageURI = uri
                binding.signupProfileImg.setImageURI(uri)
                ImageCheck = 1
            }
        }
    }

    fun upload(
        uri: Uri,
        email: String,
    ) {
        val storageRef = storage.reference
        val fileName = email + ".jpg"
        val riversRef = storageRef.child("/$fileName")

        riversRef.putFile(uri)
            .addOnProgressListener { taskSnapshot ->
                riversRef.downloadUrl.addOnSuccessListener { uri ->
                    FirebaseFirestore.getInstance()
                        .collection("EternalReturnInfo")
                        .document(auth.uid!!)
                        .update("profile",uri.toString())
                }
            }
            .addOnFailureListener { Log.i("업로드 실패", "") }
            .addOnSuccessListener { Log.i("업로드 성공", "") }
    }

}

