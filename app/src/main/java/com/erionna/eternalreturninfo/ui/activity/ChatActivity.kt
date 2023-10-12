package com.erionna.eternalreturninfo.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import com.erionna.eternalreturninfo.databinding.ChatActivityBinding
import com.erionna.eternalreturninfo.model.ERModel
import com.erionna.eternalreturninfo.util.Constants.Companion.EXTRA_ER_MODEL

class ChatActivity : AppCompatActivity() {

    companion object {
        fun newIntent(
            context: Context,
            erModel: ERModel
        ) = Intent(context, ChatActivity::class.java).apply{
            putExtra(EXTRA_ER_MODEL, erModel)
        }

    }

    private lateinit var binding: ChatActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ChatActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}