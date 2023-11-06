package com.erionna.eternalreturninfo.ui.fragment.board

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.erionna.eternalreturninfo.databinding.BoardFragmentBinding
import com.erionna.eternalreturninfo.model.BoardModel
import com.erionna.eternalreturninfo.retrofit.FBRef
import com.erionna.eternalreturninfo.ui.activity.board.BoardAdd
import com.erionna.eternalreturninfo.ui.activity.board.BoardSearch
import com.erionna.eternalreturninfo.ui.adapter.board.BoardViewPagerAdapter
import com.erionna.eternalreturninfo.ui.viewmodel.BoardListViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue


class BoardFragment : Fragment() {
    companion object {
        fun newInstance() = BoardFragment()
    }

    private var _binding: BoardFragmentBinding? = null
    private val binding get() = _binding!!

    private val boardViewModel: BoardListViewModel by activityViewModels()

    private val addBoardLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val board = result.data?.getParcelableExtra("board", BoardModel::class.java)
                    if (board != null) {
                        boardViewModel.addBoard(board)
                    }
                } else {
                    val board = result.data?.getParcelableExtra<BoardModel>("board")
                    if (board != null) {
                        boardViewModel.addBoard(board)
                    }
                }

            }else{

            }
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

        val adapter = BoardViewPagerAdapter(requireActivity())
        boardViewpager.adapter = adapter

        TabLayoutMediator(boardTabLayout, boardViewpager) { tab, position ->
            tab.setText(adapter.getTitle(position))
        }.attach()

        boardFab.setOnClickListener {
            val intent = Intent(requireContext(), BoardAdd::class.java)
            addBoardLauncher.launch(intent)
        }

        boardIbProfile.setOnClickListener {
            val intent = Intent(requireContext(), BoardSearch::class.java)
            startActivity(intent)
        }

        boardSwipeRefreshLayout.setDistanceToTriggerSync(600)

        boardSwipeRefreshLayout.setOnRefreshListener {

            FBRef.postRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val unsortedBoardList = mutableListOf<BoardModel>()
                    for (data in snapshot.children) {
                        val board = data.getValue<BoardModel>()
                        if (board != null) {
                            unsortedBoardList.add(board)
                        }
                    }

                    boardViewModel.initBoard(unsortedBoardList)

                }

                override fun onCancelled(error: DatabaseError) {
                    // 에러 처리
                }
            })

            boardSwipeRefreshLayout.isRefreshing = false
        }

    }


}