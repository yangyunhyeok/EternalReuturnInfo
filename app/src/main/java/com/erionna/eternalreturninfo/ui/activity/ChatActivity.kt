package com.erionna.eternalreturninfo.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.erionna.eternalreturninfo.databinding.ChatActivityBinding
import com.erionna.eternalreturninfo.model.ERModel
import com.erionna.eternalreturninfo.util.Constants.Companion.EXTRA_ER_MODEL
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {

    companion object {
//        fun newIntent(
//            context: Context,
//            erModel: ERModel
//        ) = Intent(context, ChatActivity::class.java).apply{
//            putExtra(EXTRA_ER_MODEL, erModel)
//        }
    }

    private lateinit var binding: ChatActivityBinding
    private lateinit var database: DatabaseReference

    // 대화상대 정보
    private lateinit var receiverName: String
    private lateinit var receiverUid: String

    private lateinit var receiverRoom: String // 받는 대화방
    private lateinit var senderRoom: String // 보내는 대화방
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ChatActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference

        val data = intent.getParcelableExtra<ERModel>("testParse")
        Log.d("#choco5732", "$data")

        binding.chatToolbarTitle.text = data?.name

        receiverName = data?.name!!
//        receiverUid = data.uId!!
    }
}