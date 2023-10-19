package com.erionna.eternalreturninfo.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erionna.eternalreturninfo.databinding.ChatItemReceiverBinding
import com.erionna.eternalreturninfo.databinding.ChatItemSenderBinding
import com.erionna.eternalreturninfo.model.Message
import com.google.firebase.auth.FirebaseAuth

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

            chatItemSenderContainer.setOnClickListener {
                onClickItem(
                    position
                )
            }
        }
    }

    class ReceiverViewHolder (
        val binding: ChatItemReceiverBinding,
        private val onClickItem: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Message) = with(binding) {
            val sb = StringBuilder()
            sb.append(item.time)
            val time = sb.substring(14,24)

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