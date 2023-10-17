package com.erionna.eternalreturninfo.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erionna.eternalreturninfo.R
import com.erionna.eternalreturninfo.databinding.ChatListItemBinding
import com.erionna.eternalreturninfo.model.ERModel
import com.erionna.eternalreturninfo.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ChatListAdapter(
    private val onClickItem: (Int, ERModel) -> Unit,
) : ListAdapter<ERModel, ChatListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<ERModel>() {
        override fun areItemsTheSame(
            oldItem: ERModel,
            newItem: ERModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ERModel,
            newItem: ERModel
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListAdapter.ViewHolder {
        return ViewHolder(
            ChatListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClickItem
        )
    }

    override fun onBindViewHolder(holder: ChatListAdapter.ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

   inner class ViewHolder(
        private val binding: ChatListItemBinding,
        private val onClickItem: (Int, ERModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

       private val auth = FirebaseAuth.getInstance()
       private val database = FirebaseDatabase.getInstance().reference

        fun bind(item: ERModel) = with(binding) {
            chatListName.text = item.name
            chatListProfilePicture.setImageResource(item.profilePicture!!)
            chatListMsg.text = item.msg
            chatListDate.text = item.time

//            val senderRoom = auth.currentUser?.uid + item.uid
//            var message = Message()

//            database.child("chats").child(senderRoom).child("messages")
//                .get().addOnSuccessListener {
//
//                    for(child in it.children) {
//                        message = child.getValue(Message::class.java)!!
//                    }

//                        chatListMsg.text = "${message.message}"
//                        chatListDate.text = "${message.time}"

//                    if (message.message == "") {
//                        chatListMsg.text = "최근에 대화한 내용이 없습니다."
////                        chatListDate.text = ""
//                    } else {
////                        chatListDate.text = "${message.time}"
//                    }

//        }

            chatListContainer.setOnClickListener {
                onClickItem(
                    adapterPosition,
                    item
                )
            }
        }
    }
}