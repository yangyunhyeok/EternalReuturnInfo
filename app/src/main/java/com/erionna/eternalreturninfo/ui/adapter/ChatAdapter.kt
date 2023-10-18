package com.erionna.eternalreturninfo.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erionna.eternalreturninfo.R
import com.erionna.eternalreturninfo.databinding.ChatItemReceiverBinding
import com.erionna.eternalreturninfo.databinding.ChatItemSenderBinding
import com.erionna.eternalreturninfo.databinding.ChatListItemBinding
import com.erionna.eternalreturninfo.model.ERModel
import com.erionna.eternalreturninfo.model.Message
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter(
    private val context: Context,
    private val messageList: ArrayList<Message>,
    private val onClickItem: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(
) {

    private val sender = 1
    private val receiver = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == sender) {
            SenderViewHolder(ChatItemSenderBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onClickItem
                )

        } else {
            ReceiverViewHolder(ChatItemReceiverBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onClickItem
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]

        if(holder.javaClass == SenderViewHolder::class.java) {
            val viewHolder = holder as SenderViewHolder
            viewHolder.binding.chatItemSenderText.text = currentMessage.message
            viewHolder.binding.chatItemSenderDate.text = currentMessage.time

            viewHolder.binding.chatItemSenderContainer.setOnClickListener {
                onClickItem(
                    position
                )
            }
        } else {
            val viewHolder = holder as ReceiverViewHolder
            viewHolder.binding.chatItemReceiverText.text = currentMessage.message
            viewHolder.binding.chatItemRecevierDate.text = currentMessage.time
            viewHolder.binding.chatItemReceiverContainer.setOnClickListener {
                onClickItem(
                    position
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        // 현재 메시지의 senduid와 접속자의 uid가 일치하면 전송모드, 불일치하면 수신모드
        return if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.sendId)){
            sender
        } else {
            receiver
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class SenderViewHolder (
        val binding: ChatItemSenderBinding,
        private val onClickItem: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
    }

    class ReceiverViewHolder (
        val binding: ChatItemReceiverBinding,
        private val onClickItem: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
    }
}