package com.erionna.eternalreturninfo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erionna.eternalreturninfo.databinding.BoardRvItemBinding
import com.erionna.eternalreturninfo.model.BoardModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class BoardRecyclerViewAdapter() : ListAdapter<BoardModel, BoardRecyclerViewAdapter.ViewHolder>(

    object : DiffUtil.ItemCallback<BoardModel>() {
        override fun areItemsTheSame(
            oldItem: BoardModel,
            newItem: BoardModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: BoardModel,
            newItem: BoardModel
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    interface OnItemClickListener {
        fun onItemClick(boardItem: BoardModel)
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BoardRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(
        private val binding: BoardRvItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BoardModel) = with(binding) {
            //작성자가 관리자 아이디면 title [공지]로 바꾸는 코드 추가 + 공지 고정하기..?
            boardPostTvTitle.text = "[일반]  " + item.title
            boardPostTvUser.text = item.author?.user

            boardPostTvDate.text = formatTimeOrDate(item.date)

            if(item.comments.size == 0){
                boardPostBtnComment.visibility = View.INVISIBLE
            }else{
                boardPostBtnComment.visibility = View.VISIBLE
                boardPostBtnComment.text = item.comments.size.toString()
            }

            itemView.setOnClickListener {
                onItemClickListener?.onItemClick(item)
            }

        }
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