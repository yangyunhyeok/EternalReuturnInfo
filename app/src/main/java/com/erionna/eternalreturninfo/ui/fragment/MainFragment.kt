package com.erionna.eternalreturninfo.ui.fragment.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.erionna.eternalreturninfo.databinding.MainFragmentBinding
import com.erionna.eternalreturninfo.model.VideoModel
import com.erionna.eternalreturninfo.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val adapter: MainAdapter by lazy {
        MainAdapter(requireContext())
    }
    private val gridmanager: StaggeredGridLayoutManager by lazy {
        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
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

        binding.mainRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.mainRecyclerview.adapter = adapter
        initView()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
    private fun initView() {

        GlobalScope.launch(Dispatchers.Main) {
            query = "이터널리턴"
            fetchItemResults()
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

            if (response.isSuccessful) {
                val youtubeVideo = response.body()!!
                youtubeVideo?.items?.forEach { snippet ->
                    val title = snippet.snippet.title
                    val url = snippet.snippet.thumbnails.medium.url
                    resItems.add(VideoModel(title = title, thumbnail = url,  url = "https://www.youtube.com/watch?v=${snippet.id}"))
                }
            }

            currenttoken = response.body()!!.nextPageToken

            adapter.items = resItems
            adapter.notifyDataSetChanged()
        } catch (e: Exception) {
            Log.e("#error check", "Error: ${e.message}")
        }
    }
}
