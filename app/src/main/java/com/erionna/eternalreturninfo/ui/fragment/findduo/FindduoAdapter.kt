package com.erionna.eternalreturninfo.ui.fragment.findduo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.erionna.eternalreturninfo.databinding.FindDuoListItemBinding
import com.erionna.eternalreturninfo.model.ERModel
import com.erionna.eternalreturninfo.model.User

class FindduoAdapter(
    private val context: Context,
    private val onClickUser: (Int, ERModel) -> Unit,
    private val onLongClickUser: (Int, ERModel) -> Unit
) : RecyclerView.Adapter<FindduoAdapter.ItemViewHolder>(
) {

    var items = ArrayList<ERModel>()
    override fun getItemCount(): Int {
        return items.size
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FindduoAdapter.ItemViewHolder {
        val binding =
            FindDuoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ItemViewHolder(binding, onClickUser, onLongClickUser)
    }

    override fun onBindViewHolder(holder: FindduoAdapter.ItemViewHolder, position: Int) {
        val currentItem = items[position]

        // currentItem에서 데이터를 추출하고 뷰에 설정
        holder.server.text = currentItem.server
        holder.name.text = currentItem.name
        holder.gender.text = currentItem.gender
        holder.tier.text = currentItem.tier

        Glide.with(holder.itemView.context)
            .load(currentItem.profilePicture)
            .into(holder.profilePicture)

        holder.binding.fdliContainer.setOnClickListener {
            onClickUser(
                position,
                currentItem
            )
        }

        holder.binding.fdliContainer.setOnLongClickListener {
            onLongClickUser(position, currentItem)
            true
        }
    }
    inner class ItemViewHolder(
        var binding: FindDuoListItemBinding,
        private val onClickUser: (Int, ERModel) -> Unit,
        private val onLongClickUser: (Int, ERModel) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        var server= binding.fdliServer
        var name= binding.fdliName
        var gender= binding.fdliGender
        var tier= binding.fdliTier
        var profilePicture= binding.fdliMost

        override fun onClick(view: View) {
            val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return
            val item = items[position]

            itemClickListener?.onItemClick(item)
        }


    }

    interface OnItemClickListener {
        fun onItemClick(item: ERModel)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }
}