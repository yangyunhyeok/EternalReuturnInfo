package com.erionna.eternalreturninfo.ui.fragment.findduo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erionna.eternalreturninfo.databinding.FindDuoListItemBinding
import com.erionna.eternalreturninfo.model.User

class FindduoAdapter(private val context: Context) : RecyclerView.Adapter<FindduoAdapter.ItemViewHolder>() {
    var items = ArrayList<User>()

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FindduoAdapter.ItemViewHolder {
        val binding = FindDuoListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FindduoAdapter.ItemViewHolder, position: Int) {
        val currentItem = items[position]

    }

    inner class ItemViewHolder(var binding: FindDuoListItemBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        var server: String = binding.server.toString()
        var gender: String = binding.gender.toString()
        var tier: String = binding.tier.toString()
        var most:String=binding.most.toString()

        override fun onClick(view: View) {
            val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return
            val item = items[position]

            // 클릭한 아이템을 인터페이스를 통해 SearchFragment로 전달
            itemClickListener?.onItemClick(item)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: User)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }
}