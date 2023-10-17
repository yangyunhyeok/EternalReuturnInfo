package com.erionna.eternalreturninfo.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.erionna.eternalreturninfo.R
import com.erionna.eternalreturninfo.databinding.SignupImageActivityBinding

class SignUpImagePage : AppCompatActivity() {
    private lateinit var binding:SignupImageActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupImageActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupimgNextBtn.setOnClickListener {
            val intent = Intent(this,SignUpPage::class.java)
            startActivity(intent)
            finish()
        }
    }
}