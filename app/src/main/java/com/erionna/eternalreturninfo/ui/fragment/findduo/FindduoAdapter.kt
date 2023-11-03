package com.erionna.eternalreturninfo.ui.fragment.findduo

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.erionna.eternalreturninfo.R
import com.erionna.eternalreturninfo.databinding.FindDuoListItemBinding
import com.erionna.eternalreturninfo.model.ERModel
import com.erionna.eternalreturninfo.model.User
import com.erionna.eternalreturninfo.retrofit.BoardSingletone
import com.erionna.eternalreturninfo.retrofit.RetrofitInstance
import com.erionna.eternalreturninfo.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        StatePacth().setState(currentItem.name,holder.binding.fdliWinrate,holder.binding.fdliAvgrank)

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
                R.drawable.ic_xiuk,
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
                R.drawable.ic_xiuk,
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

    class StatePacth{

        fun setState(name:String?, textView: TextView, textView2: TextView){
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val nickname = name

                    //카드에 배치된 사람 닉네임 가져오기
                    val userID_call = RetrofitInstance.search_userID_api.getUserByNickname(Constants.MAIN_APIKEY, nickname)
                    val userID_response = userID_call.execute()

                    if (userID_response.isSuccessful) {
                        val gameResponse = userID_response.body()
                        val userNum = gameResponse?.user?.userNum.toString()
                        val seasonId = "19"

                        val userstate_call = RetrofitInstance.search_user_state_api.getUserStats(
                            Constants.MAIN_APIKEY, userNum, seasonId)
                        val userstate_response = userstate_call.execute()

                        if (userstate_response.isSuccessful) {
                            val userStateResponse = userstate_response.body()

                            withContext(Dispatchers.Main) {

                                val user = userStateResponse?.userStats?.get(0)

                                textView.text = (user?.top1?.times(100) ?: 0).toString() + "%"
                                textView2.text = "#"+(user?.averageRank ?: 0).toString()
                            }

                        } else {
                            Log.d("userStateResponse", "${userstate_response}")
                        }
                    }

                } catch (e: Exception) {
                    // 오류 처리
                    e.printStackTrace()
                }
            }
        }
    }

}