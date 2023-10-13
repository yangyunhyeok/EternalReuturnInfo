package com.erionna.eternalreturninfo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erionna.eternalreturninfo.databinding.BoardPostRvCommentItemBinding
import com.erionna.eternalreturninfo.model.BoardModel

class BoardCommentRecyclerViewAdpater() : ListAdapter<BoardModel, BoardCommentRecyclerViewAdpater.ViewHolder>(

    object : DiffUtil.ItemCallback<BoardModel>() {
        override fun areItemsTheSame(
            oldItem: BoardModel,
            newItem: BoardModel
        ): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(
            oldItem: BoardModel,
            newItem: BoardModel
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

        fun bind(item: BoardModel) = with(binding) {

            boardCommentTvUser.text = item.comment.user
            boardCommentTvContent.text = item.comment.content
            boardCommentTvDate.text = item.comment.date

        }
    }

}