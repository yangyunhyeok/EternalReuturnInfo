package com.erionna.eternalreturninfo.ui.fragment.myprofile

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.erionna.eternalreturninfo.R
import com.erionna.eternalreturninfo.databinding.BoardRvFragmentBinding
import com.erionna.eternalreturninfo.databinding.MyprofileRecordFragmentBinding
import com.erionna.eternalreturninfo.model.BoardModel
import com.erionna.eternalreturninfo.retrofit.BoardSingletone
import com.erionna.eternalreturninfo.retrofit.CharacterStats
import com.erionna.eternalreturninfo.retrofit.FBRef
import com.erionna.eternalreturninfo.retrofit.RetrofitInstance
import com.erionna.eternalreturninfo.ui.activity.board.BoardDeleted
import com.erionna.eternalreturninfo.ui.activity.board.BoardPost
import com.erionna.eternalreturninfo.ui.adapter.board.BoardMyProfileRecyclerViewAdapter
import com.erionna.eternalreturninfo.ui.adapter.board.BoardRecyclerViewAdapter
import com.erionna.eternalreturninfo.ui.adapter.myprofile.MyprofileListAdapter
import com.erionna.eternalreturninfo.ui.viewmodel.BoardListViewModel
import com.erionna.eternalreturninfo.util.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyprofileRecordFragment : Fragment() {
    companion object {
        fun newInstance() = MyprofileRecordFragment()
    }

    private var _binding: MyprofileRecordFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MyprofileRecordFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
//        initModel()

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initView() = with(binding) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val nickname = BoardSingletone.LoginUser().name.toString()

                //수정 : 로그인한 사람 닉네임 가져오기
                val userID_call = RetrofitInstance.search_userID_api.getUserByNickname(
                    Constants.MAIN_APIKEY,
                    nickname
                )
                val userID_response = userID_call.execute()

                if (userID_response.isSuccessful) {
                    val gameResponse = userID_response.body()
                    val userNum = gameResponse?.user?.userNum.toString()
                    val seasonId = "19"

                    val userstate_call = RetrofitInstance.search_user_state_api.getUserStats(
                        Constants.MAIN_APIKEY, userNum, seasonId
                    )
                    val userstate_response = userstate_call.execute()

                    if (userstate_response.isSuccessful) {
                        val userStateResponse = userstate_response.body()


                        withContext(Dispatchers.Main) {
                            val user = userStateResponse?.userStats?.get(0)
//                            binding.myprofileTvTop1.text =
//                                (user?.top1?.times(100) ?: 0).toString() + "%"
//                            binding.myprofileTvAverageRank.text =
//                                "#" + (user?.averageRank ?: 0).toString()
//                            binding.myprofileTvAverageKill.text =
//                                (user?.averageKills ?: 0).toString()

                            var dataList = mutableListOf<CharacterStats>()
                            for(a in 0..2){
                                dataList.add(CharacterStats(user!!.characterStats[a].characterCode,user.characterStats[a].totalGames,user.characterStats[a].usages,user.characterStats[a].maxKillings,user.characterStats[a].top3,user.characterStats[a].wins,user.characterStats[a].top3Rate,user.characterStats[a].averageRank))
                            }
                            Log.d("마이페이지 데이터리스트","$dataList")

                            val array: Array<String> = resources.getStringArray(R.array.characterName)
                            val adapter = MyprofileListAdapter(dataList, array)
                            binding.myprofileCharacterRv.adapter = adapter
                            binding.myprofileCharacterRv.layoutManager = LinearLayoutManager(requireContext())
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

//    private fun initModel() = with(boardViewModel) {
//
//    }
}