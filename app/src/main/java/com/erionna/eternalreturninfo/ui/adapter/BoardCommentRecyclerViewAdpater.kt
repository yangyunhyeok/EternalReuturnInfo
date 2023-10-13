package com.erionna.eternalreturninfo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erionna.eternalreturninfo.databinding.BoardPostRvCommentItemBinding
import com.erionna.eternalreturninfo.model.CommentModel

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
            boardCommentTvDate.text = item.date

        }
    }

}