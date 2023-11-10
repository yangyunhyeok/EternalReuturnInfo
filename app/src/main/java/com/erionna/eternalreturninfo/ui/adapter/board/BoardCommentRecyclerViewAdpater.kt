package com.erionna.eternalreturninfo.ui.adapter.board

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.erionna.eternalreturninfo.R
import com.erionna.eternalreturninfo.databinding.BoardPostRvCommentItemBinding
import com.erionna.eternalreturninfo.model.CommentModel
import com.erionna.eternalreturninfo.model.ERModel
import com.erionna.eternalreturninfo.retrofit.BoardSingletone
import com.erionna.eternalreturninfo.retrofit.FBRef
import com.erionna.eternalreturninfo.ui.activity.board.BoardDialog
import com.erionna.eternalreturninfo.ui.activity.chat.ChatActivity
import com.erionna.eternalreturninfo.ui.activity.board.DialogListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class BoardCommentRecyclerViewAdpater() : ListAdapter<CommentModel, BoardCommentRecyclerViewAdpater.ViewHolder>(

    object : DiffUtil.ItemCallback<CommentModel>() {
        override fun areItemsTheSame(
            oldItem: CommentModel,
            newItem: CommentModel
        ): Boolean {
            return oldItem.author == newItem.author
        }

        override fun areContentsTheSame(
            oldItem: CommentModel,
            newItem: CommentModel
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    interface OnItemClickListener {
        fun onDeleteItemClick(commentItem: CommentModel, position:Int)
        fun onUpdateItemClick(commentItem: CommentModel, position:Int)
    }

    private var onDeleteItemClickListener: OnItemClickListener? = null
    private var onUpdateItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onDeleteItemClickListener = listener
        this.onUpdateItemClickListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BoardPostRvCommentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: BoardPostRvCommentItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CommentModel) = with(binding) {

            FBRef.userRef.child(item?.author.toString()).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if(snapshot.exists()){
                        val author = snapshot.getValue<ERModel>()

                        boardCommentTvUser.text = author?.name

                        if(author?.profilePicture?.isEmpty() == true){
                            boardCommentIbProfile.setImageResource(R.drawable.ic_baseimage)
                        }else{
                            boardCommentIbProfile.load(author?.profilePicture)
                        }

                        //로그인한 사용자면 ibMenu 보여주기
                        if (author?.uid == BoardSingletone.LoginUser().uid) {
                            boardCommentIbMenu.visibility = View.VISIBLE
                            boardCommentIbMenu.setOnClickListener {
                                val popup = PopupMenu(binding.root.context, boardCommentIbMenu) // View 변경
                                popup.menuInflater.inflate(R.menu.menu_option_comment, popup.menu)
                                popup.setOnMenuItemClickListener { menu ->
                                    when (menu.itemId) {
                                        R.id.menu_comment_update -> {
                                            onUpdateItemClickListener?.onUpdateItemClick(item, adapterPosition)
                                        }
                                        R.id.menu_comment_delete -> {
                                            onDeleteItemClickListener?.onDeleteItemClick(item, adapterPosition)
                                        }
                                    }
                                    false
                                }
                                popup.show()
                            }
                        } else {
                            boardCommentIbMenu.visibility = View.INVISIBLE

                            boardCommentIbProfile.setOnClickListener {
                                val customDialog = BoardDialog(binding.root.context, author?.name.toString(), object : DialogListener {
                                    override fun onOKButtonClicked() {
                                        val intent = ChatActivity.newIntent(binding.root.context,
                                            ERModel(
                                                uid = author?.uid,
                                                profilePicture = author?.profilePicture,
                                                name = author?.name
                                            )
                                        )
                                        binding.root.context.startActivity(intent)
                                    }
                                })

                                customDialog.show()
                            }
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                }

            })


            boardCommentTvContent.text = item.content
            boardCommentTvDate.text = formatTimeOrDate(item.date)

        }

        fun formatTimeOrDate(postTime: Long): String {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)

            val date1 = calendar.time

            val simpleDateFormat: SimpleDateFormat
            if (Date(postTime) > date1) {
                simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            } else {
                simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            }

            return simpleDateFormat.format(Date(postTime))
        }
    }

}