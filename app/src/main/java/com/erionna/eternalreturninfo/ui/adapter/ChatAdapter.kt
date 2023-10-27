package com.erionna.eternalreturninfo.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.erionna.eternalreturninfo.databinding.ChatItemReceiverBinding
import com.erionna.eternalreturninfo.databinding.ChatItemSenderBinding
import com.erionna.eternalreturninfo.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatAdapter(
    private val messageList: ArrayList<Message>,
    private val onClickItem: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(
) {
    enum class ItemViewType {
        SENDER, RECEIVER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (
            viewType == ItemViewType.SENDER.ordinal
        ) {
            SenderViewHolder(
                ChatItemSenderBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onClickItem
            )
        } else {
            ReceiverViewHolder(
                ChatItemReceiverBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onClickItem
            )
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder, position: Int
    ) {
        val item = messageList[position]

        if (holder.javaClass == SenderViewHolder::class.java) {
            holder as SenderViewHolder
            holder.bind(item)
        } else {
            holder as ReceiverViewHolder
            holder.bind(item)
        }
    }

    override fun getItemViewType(
        position: Int
    ): Int {
        val currentMessage = messageList[position]
        // 현재 메시지의 senduid와 접속자의 uid가 일치하면 전송모드, 불일치하면 수신모드
        return if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.sendId)) {
            ItemViewType.SENDER.ordinal
        } else {
            ItemViewType.RECEIVER.ordinal
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class SenderViewHolder(
        val binding: ChatItemSenderBinding,
        private val onClickItem: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Message) = with(binding) {
            val sb = StringBuilder()
            sb.append(item.time)
            val time = sb.substring(14, 24)
            chatItemSenderDate.text = time
            chatItemSenderText.text = item.message

            val database = FirebaseDatabase.getInstance().reference
            var recevierRoom: String = item.sendId + item.receiverId


            // 수정한 코드 ( 고쳐서 잘 작동은 하지만.. 왜 잘 작동하는지..)
            database.child("chats").child(recevierRoom).child("messages")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapShot: DataSnapshot) {
                        var receiverReadOrNot: Boolean? = null
                        for (child in snapShot.children) {
                            val message = child.getValue(Message::class.java)
                            if (message?.id == item.id) {
                                receiverReadOrNot = message?.readOrNot
                            }
                        }
                        if (receiverReadOrNot == false) {
                            chatItemSenderReadCount.visibility = View.VISIBLE
                        } else {
                            chatItemSenderReadCount.visibility = View.GONE
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })

//             기존 에러 코드
//            database.child("chats").child(recevierRoom).child("messages")
//                .addValueEventListener(object : ValueEventListener {
//                    override fun onDataChange(snapShot: DataSnapshot) {
//                        var receiverReadOrNot: Boolean? = null
//                        for (child in snapShot.children) {
//                            val message = child.getValue(Message::class.java)
//                            receiverReadOrNot = message?.readOrNot
//                        }
//                        if ( receiverReadOrNot == true ) {
//                            chatItemSenderReadCount.visibility = View.INVISIBLE
//                        } else {
//                            chatItemSenderReadCount.visibility = View.VISIBLE
//                        }
//                    }
//                    override fun onCancelled(error: DatabaseError) {
//                        TODO("Not yet implemented")
//                    }
//                })

            chatItemSenderContainer.setOnClickListener {
                onClickItem(
                    position
                )
            }
        }
    }

    class ReceiverViewHolder(
        val binding: ChatItemReceiverBinding,
        private val onClickItem: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Message) = with(binding) {
            val sb = StringBuilder()
            sb.append(item.time)
            val time = sb.substring(14, 24)

            chatItemRecevierDate.text = time
            chatItemReceiverText.text = item.message

            chatItemReceiverContainer.setOnClickListener {
                onClickItem(
                    position
                )
            }
        }
    }
}

//            database.child("chats").child(recevierRoom).child("messages").get().addOnSuccessListener {
//                for ( child in it.children ) {
//                    val message = child.getValue(Message::class.java)
//                    val readOrNot = message?.readOrNot
//                    if ( readOrNot == true ) {
//                        chatItemSenderReadCount.visibility = View.INVISIBLE
//                    } else {
//                        chatItemSenderReadCount.visibility = View.VISIBLE
//                    }
//                }
//            }


//database.child("chats").child(recevierRoom).child("messages")
//.addValueEventListener(object : ValueEventListener {
//    override fun onDataChange(snapShot: DataSnapshot) {
//        var receiverReadOrNot: Boolean? = null
//        for (postSnapshot in snapShot.children) {
//            val message = postSnapshot.getValue(Message::class.java)
//            receiverReadOrNot = message?.readOrNot
//            if ( receiverReadOrNot == true ) {
//                chatItemSenderReadCount.visibility = View.INVISIBLE
//            } else {
//                chatItemSenderReadCount.visibility = View.VISIBLE
//            }
//        }
//    }
//    override fun onCancelled(error: DatabaseError) {
//        TODO("Not yet implemented")
//    }
//})