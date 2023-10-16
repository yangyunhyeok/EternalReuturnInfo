package com.erionna.eternalreturninfo.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erionna.eternalreturninfo.databinding.BoardPostRvCommentItemBinding
import com.erionna.eternalreturninfo.model.CommentModel
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

            boardCommentTvUser.text = item.author
            boardCommentTvContent.text = item.content

            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)

            val standardTime = calendar.time

            boardCommentTvDate.text = formatTimeOrDate(item.date)

            //로그인한 사용자면 ibMenu 보여주기
            if(item.author == "user2"){
                boardCommentIbMenu.visibility = View.VISIBLE
            }else{
                boardCommentIbMenu.visibility = View.INVISIBLE
            }

        }

        fun formatTimeOrDate(postTime: Long): String {

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)

            val calendar2 = Calendar.getInstance()
            calendar2.set(Calendar.HOUR_OF_DAY, 23)
            calendar2.set(Calendar.MINUTE, 59)
            calendar2.set(Calendar.SECOND, 59)

            val date1 = calendar.time
            val date2 = calendar2.time

            if(date1 <= Date(postTime) && Date(postTime) <= date2){
                val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                return simpleDateFormat.format(Date(postTime))
            }else{
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                return simpleDateFormat.format(Date(postTime))
            }

        }
    }

}