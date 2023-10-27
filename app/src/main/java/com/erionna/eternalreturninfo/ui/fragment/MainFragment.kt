package com.erionna.eternalreturninfo.ui.fragment.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.erionna.eternalreturninfo.databinding.MainFragmentBinding
import com.erionna.eternalreturninfo.model.Notice
import com.erionna.eternalreturninfo.model.VideoModel
import com.erionna.eternalreturninfo.retrofit.BoardSingletone
import com.erionna.eternalreturninfo.retrofit.RetrofitInstance
import com.erionna.eternalreturninfo.ui.activity.WebView
import com.erionna.eternalreturninfo.ui.adapter.NoticeBannerListAdapter
import com.erionna.eternalreturninfo.ui.adapter.VideoListAdapter
import com.erionna.eternalreturninfo.ui.fragment.LinePagerIndicatorDecoration
import com.erionna.eternalreturninfo.ui.fragment.SnapPagerScrollListener
import com.erionna.eternalreturninfo.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val noticeListAdapter by lazy {
        NoticeBannerListAdapter()
    }

    private val videoListAdapter by lazy {
        VideoListAdapter()
    }

    private val resItems: ArrayList<VideoModel> = ArrayList()
    private var query:String =""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainRvYoutube.adapter = videoListAdapter
        binding.mainRvYoutube.layoutManager = LinearLayoutManager(requireContext())

        binding.mainBannerNotice.adapter = noticeListAdapter
        val bannerVideoLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.mainBannerNotice.layoutManager = bannerVideoLayoutManager

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.mainBannerNotice)

        val listener = SnapPagerScrollListener(
            snapHelper,
            SnapPagerScrollListener.ON_SCROLL,
            true,
            object : SnapPagerScrollListener.OnChangeListener {
                override fun onSnapped(position: Int) {
                    // position을 받아서 이벤트 처리
                }
            }
        )
        binding.mainBannerNotice.addOnScrollListener(listener)
        binding.mainBannerNotice.addItemDecoration(LinePagerIndicatorDecoration())

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val call = RetrofitInstance.eternal_api.getNews("locale=ko_KR")
                val response = call.execute()

                if (response.isSuccessful) {
                    val responseBody = response.body()

                    if (responseBody != null) {
                        val articles = responseBody.articles

                        val list = mutableListOf<Notice>()
                        articles.mapNotNull { list.add(Notice(it.thumbnail_url, it.i18ns["ko_KR"]?.title, it.i18ns["ko_KR"]?.summary, it.i18ns["ko_KR"]?.created_at_for_humans, it.url))}

                        withContext(Dispatchers.Main) {

                            noticeListAdapter.submitList(list)

                            noticeListAdapter.setOnItemClickListener(object : NoticeBannerListAdapter.OnItemClickListener{
                                override fun onItemClick(item: Notice, position:Int) {
                                    val intent = Intent(requireContext(), com.erionna.eternalreturninfo.ui.activity.WebView::class.java)
                                    intent.putExtra("url", list[position].url)
                                    startActivity(intent)
                                }
                            })
                        }

                    }
                } else {
                    println("API 요청 실패: ${response.code()}")
                }

                val nickname = fetchNonNullableNickname()

                //수정 : 로그인한 사람 닉네임 가져오기
                val userID_call = RetrofitInstance.search_userID_api.getUserByNickname(Constants.MAIN_APIKEY, nickname)
                val userID_response = userID_call.execute()

                if (userID_response.isSuccessful) {
                    val gameResponse = userID_response.body()
                    val userNum = gameResponse?.user?.userNum.toString()
                    val seasonId = "19"

                    val userstate_call = RetrofitInstance.search_user_state_api.getUserStats(Constants.MAIN_APIKEY, userNum, seasonId)
                    val userstate_response = userstate_call.execute()

                    if (userstate_response.isSuccessful) {
                        val userStateResponse = userstate_response.body()

                        withContext(Dispatchers.Main) {

                            val user = userStateResponse?.userStats?.get(0)

                            binding.mainTvRp.text = (user?.mmr ?: 0).toString() + " PR"
                            binding.mainTvRank.text = "${(user?.rank ?: 0)}위 (상위 ${(user?.rankPercent?.times(100) ?: 0)}%)"
                            binding.mainTvTotalWin.text = (user?.totalWins ?: 0).toString()
                            binding.mainTvTop1.text = (user?.top1?.times(100) ?: 0).toString()
                            binding.mainTvTotalGames.text = (user?.totalGames ?: 0).toString()
                            binding.mainTvAverageKill.text = (user?.averageKills ?: 0).toString()
                            binding.mainTvTop2.text = (user?.top2?.times(100) ?: 0).toString()
                            binding.mainTvAverageRank.text = (user?.averageRank ?: 0).toString()
                            binding.mainTvAverageAssiants.text = (user?.averageAssistants ?: 0).toString()
                            binding.mainTvTop3.text = (user?.top3?.times(100) ?: 0).toString()

                            binding.mainPbTop1.setProgress(user?.top1?.times(100)?.toInt() ?: 0)
                            binding.mainPbTop2.setProgress(user?.top2?.times(100)?.toInt() ?: 0)
                            binding.mainPbTop3.setProgress(user?.top3?.times(100)?.toInt() ?: 0)
                            binding.mainPbAverageKill.setProgress(user?.averageKills?.toInt() ?: 0)
                            binding.mainPbAverageAssiants.setProgress(user?.averageAssistants?.toInt() ?: 0)
                            binding.mainPbAverageRank.setProgress(user?.averageRank?.toInt() ?: 0)
                        }

                    } else {
                        Log.d("userStateResponse", "${userstate_response}")
                    }
                } else {
                    Log.d("gameResponse", "${response}")
                }

            } catch (e: Exception) {
                // 오류 처리
                e.printStackTrace()
            }
        }

        initView()
    }

    private suspend fun fetchNonNullableNickname(): String {
        var nickname: String? = null
        while (nickname == null) {
            nickname = BoardSingletone.LoginUser().name
            if (nickname == null) {
                // 닉네임이 null인 경우 잠시 대기하거나 다른 작업 수행
                delay(1000)
            }
        }
        return nickname
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
    private fun initView() = with(binding){

        GlobalScope.launch(Dispatchers.Main) {
            query = "이터널리턴"
            fetchItemResults()
        }

        videoListAdapter.setOnItemClickListener(object : VideoListAdapter.OnItemClickListener{
            override fun onItemClick(item: VideoModel, position: Int) {
                Log.d("item.id", item.id.toString())
                val videoUrl = "https://www.youtube.com/watch?v=${item.id}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl))
                intent.setPackage("com.google.android.youtube")
                startActivity(intent)
            }

        })

        mainBtnSeeMore.setOnClickListener {
            val intent = Intent(requireContext(), WebView::class.java)
            intent.putExtra("url", "https://dak.gg/er")
            startActivity(intent)
        }

    }

    private lateinit var currenttoken: String

    private suspend fun fetchItemResults() {
        try {
            val response = RetrofitInstance.api.getYouTubeVideos(
                query = query,
                maxResults = 10,
                videoOrder = "relevance"
            )

            resItems.clear()

            if (response.isSuccessful) {
                val youtubeVideo = response.body()!!
                Log.d("youtubeVideo", youtubeVideo.toString())
                youtubeVideo?.items?.forEach { snippet ->
                    val title = snippet.snippet.title
                    val url = snippet.snippet.thumbnails.medium.url
                    resItems.add(VideoModel(id= snippet.id.videoId, title = title, thumbnail = url,  url = "https://www.youtube.com/watch?v=${snippet.id}"))
                }
            }else{
                if (response.code() == 403 || response.code() == 429) {
                    Toast.makeText(
                        requireContext(),
                        "API 호출 제한 오류! 나중에 다시 시도해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            currenttoken = response.body()!!.nextPageToken

            videoListAdapter.submitList(resItems.toMutableList())

        } catch (e: Exception) {
            Log.e("#error check", "Error: ${e.message}")
        }
    }
}
