package com.erionna.eternalreturninfo.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.erionna.eternalreturninfo.databinding.ChatActivityBinding
import com.erionna.eternalreturninfo.model.ERModel
import com.erionna.eternalreturninfo.model.Message
import com.erionna.eternalreturninfo.ui.adapter.ChatAdapter
import com.erionna.eternalreturninfo.util.Constants.Companion.EXTRA_ER_MODEL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {

    companion object {
        fun newIntent(
            context: Context,
            erModel: ERModel
        ): Intent {
            val intent = Intent(context, ChatActivity::class.java).apply {
                putExtra(EXTRA_ER_MODEL, erModel)
            }
            return intent
        }
    }

    private lateinit var binding: ChatActivityBinding

    // 파이어베이스 객체
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    // 대화상대 정보
    private lateinit var receiverName: String
    private lateinit var receiverUid: String

    // 송수신 대화방
    private lateinit var receiverRoom: String
    private lateinit var senderRoom: String

    private lateinit var messageList : ArrayList<Message>

    private val chatAdapter by lazy {
        ChatAdapter(
            this,
            messageList
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ChatActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 파이어베이스 객체 초기화
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        // 채팅목록에서 전달받은 상대방 데이터 저장
        val data = intent.getParcelableExtra<ERModel>(EXTRA_ER_MODEL)
        Log.d("#choco5732", "$data")

        // 툴바에 채팅상대 이름 출력하기
        binding.chatToolbarTitle.text = data?.name

        receiverName = data?.name.toString()
        receiverUid = data?.uid.toString()
        Log.d("#choco5732", "receiverName : $receiverName  , receverUid = $receiverUid")

        // 초기화
        messageList = ArrayList()
        binding.chatRecycler.adapter = chatAdapter
        binding.chatRecycler.layoutManager = LinearLayoutManager(this)
        // 로그인 한 사용자 uid
        val senderUid = auth.currentUser?.uid

        // 보낸이 방
        senderRoom = receiverUid + senderUid
        // 받는이 방
        receiverRoom = senderUid + receiverUid
        Log.d("#choco5732", "senderRoom : $senderRoom")

        // 전송버튼 클릭 시 -> et의 내용은 db에 저장되고 입력한 값이 화면에 출력
        binding.chatSendBtn.setOnClickListener {
            // et에 입력한 메시지
            val message = binding.chatMsgEt.text.toString()
            val messageObject = Message(message, senderUid)

            if (message != "") {
                // db에 메시지 저장 ( 송수신 방 둘 다 저장)
                database.child("chats").child(senderRoom).child("messages").push()
                    .setValue(messageObject).addOnSuccessListener {
                        database.child("chats").child(receiverRoom).child("messages").push()
                            .setValue(messageObject)
                    }

                // 메시지 전송 후 EditText 공백 처리
                binding.chatMsgEt.setText("")

                val imm =
                    this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.chatMsgEt.windowToken, 0)

            }
        }
//
//        binding.chatRecycler.setOnClickListener{
//            val imm =
//                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(binding.chatMsgEt.windowToken, 0)
//        }

//        binding.chatActivityRecyclerContainer.setOnClickListener {
//            val imm =
//                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(binding.chatMsgEt.windowToken, 0)
//        }
//
//        binding.chatToolbar.setOnClickListener {
//            val imm =
//                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(binding.chatMsgEt.windowToken, 0)
//        }


//         메시지 가져오기
        database.child("chats").child(senderRoom).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapShot: DataSnapshot) {
                    binding.chatRecycler.scrollToPosition(messageList.size - 1) // 새로운 메시지 송, 수신시 최하단 화면으로 이동
                    messageList.clear()

                    for (postSnapshot in snapShot.children) {
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    chatAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }

//    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
//        // 키보드 숨기기
//        val imm =
//            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
//        return super.dispatchTouchEvent(ev)
//    }
}