package com.erionna.eternalreturninfo.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.erionna.eternalreturninfo.databinding.BoardFragmentBinding
import com.erionna.eternalreturninfo.model.BoardModel
import com.erionna.eternalreturninfo.model.Comment
import com.erionna.eternalreturninfo.ui.activity.BoardAdd
import com.erionna.eternalreturninfo.ui.activity.BoardPost
import com.erionna.eternalreturninfo.ui.adapter.BoardRecyclerViewAdapter

class BoardFragment : Fragment() {
    companion object {
        fun newInstance() = BoardFragment()
    }

    private var _binding: BoardFragmentBinding? = null
    private val binding get() = _binding!!

    private val listAdapter by lazy {
        BoardRecyclerViewAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BoardFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initView() = with(binding) {
        boardRv.adapter = listAdapter
        boardRv.layoutManager = LinearLayoutManager(requireContext())
        val list = mutableListOf<BoardModel>()
        list.add(BoardModel("[공지]   게시판 공지!!", "공지 내용입니다!", "게시판 관리자", "2023.10.11", Comment("ER 짱", "넵! 잘 지키겠습니다.", "2023.10.12"), 2))
        list.add(BoardModel("[일반]   캐릭터어쩌구저쩌구", "캐릭터는 이게 짱짱!", "ER 짱!", "2023.10.11", Comment("ER 짱", "넵! 잘 지키겠습니다.", "2023.10.12"), 0))
        listAdapter.submitList(list)

        listAdapter.setOnItemClickListener(object : BoardRecyclerViewAdapter.OnItemClickListener{
            override fun onItemClick(boardItem: BoardModel) {
                val intent = Intent(requireContext(), BoardPost::class.java)
                startActivity(intent)
            }
        })


        boardFab.setOnClickListener {
            val intent = Intent(requireContext(), BoardAdd::class.java)
            startActivity(intent)
        }
    }
}