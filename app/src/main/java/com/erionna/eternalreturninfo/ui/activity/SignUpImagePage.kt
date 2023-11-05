package com.erionna.eternalreturninfo.ui.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.erionna.eternalreturninfo.R
import com.erionna.eternalreturninfo.databinding.SignupImageActivityBinding

class SignUpImagePage : AppCompatActivity() {
    private lateinit var binding:SignupImageActivityBinding
    private lateinit var selectedImageURI : Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupImageActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupimgNextBtn.setOnClickListener {
            val intent = Intent(this,SignUpPage::class.java)
            intent.putExtra("uri",selectedImageURI)
            Log.d("버튼클릭","$selectedImageURI")
            startActivity(intent)
            finish()
        }
    }



}