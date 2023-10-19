package com.erionna.eternalreturninfo.ui.fragment.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.erionna.eternalreturninfo.R
import com.erionna.eternalreturninfo.databinding.MainVideoItemBinding
import com.erionna.eternalreturninfo.model.VideoModel

class MainAdapter(private val context: Context) : RecyclerView.Adapter<MainAdapter.ItemViewHolder>() {
    var items = ArrayList<VideoModel>()

    fun itemClear(){
        items.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = MainVideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = items[position]

        Glide.with(context)
            .load(currentItem.thumbnail)
            .placeholder(R.drawable.ic_white_logo)
            .into(holder.thumbNailImage)


        holder.title.text = currentItem.title

    }

    inner class ItemViewHolder(var binding: MainVideoItemBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        var thumbNailImage: ImageView = binding.searchThumbnail
        var title: TextView = binding.searchTitle
        var thumbNailItem: ConstraintLayout = binding.thumbnailItem

        init {
            thumbNailImage.setOnClickListener(this)
            thumbNailItem.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return
            val item = items[position]

            // 클릭한 아이템을 인터페이스를 통해 SearchFragment로 전달
            itemClickListener?.onItemClick(item)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: VideoModel)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }
}