package com.erionna.eternalreturninfo.ui.fragment.findduo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.erionna.eternalreturninfo.R
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

        // 이미지 설정을 위해 ImgPatch 클래스의 메서드 호출
        ImgPatch().setCharacterImage(currentItem.most, holder.binding.fdliMost)
        ImgPatch2().setTierImage(currentItem.tier,holder.binding.fdliTierimage)

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
        var most= binding.fdliMost

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

    class ImgPatch {
        fun setCharacterImage(character: String?, imageView: ImageView) {
            val resources = imageView.context.resources
            val array = resources.getStringArray(R.array.character)
            val imageArray = arrayOf(
                R.drawable.ic_character_01haze,
                R.drawable.ic_character_02xiukai,
                R.drawable.ic_character_03nadine,
                R.drawable.ic_character_04nathapon,
                R.drawable.ic_character_05nicty,
                R.drawable.ic_character_06daniel,
                R.drawable.ic_character_07tia,
                R.drawable.ic_character_08laura,
                R.drawable.ic_character_09lenox,
                R.drawable.ic_character_10leon
            )

            val index = array.indexOf(character)
            if (index != -1 && index < imageArray.size) {
                imageView.setImageResource(imageArray[index])
            }
        }
    }

    class ImgPatch2{
        fun setTierImage(tier:String?,imageView: ImageView){
            val resources = imageView.context.resources
            val array = resources.getStringArray(R.array.tier)
            val imageArray = arrayOf(
                R.drawable.ic_unrank,
                R.drawable.ic_iron,
                R.drawable.ic_bronze,
                R.drawable.ic_silver,
                R.drawable.ic_gold,
                R.drawable.ic_platinum,
                R.drawable.ic_diamond,
                R.drawable.ic_mithril,
                R.drawable.ic_titan,
                R.drawable.ic_immortal
            )
            val index = array.indexOf(tier)
            if (index != -1 && index < imageArray.size) {
                imageView.setImageResource(imageArray[index])
            }
        }
    }

}