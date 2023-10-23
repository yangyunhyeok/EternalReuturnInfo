package com.erionna.eternalreturninfo.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.erionna.eternalreturninfo.databinding.BoardFragmentBinding
import com.erionna.eternalreturninfo.model.BoardModel
import com.erionna.eternalreturninfo.retrofit.FBRef
import com.erionna.eternalreturninfo.ui.activity.BoardAdd
import com.erionna.eternalreturninfo.ui.activity.BoardDeleted
import com.erionna.eternalreturninfo.ui.activity.BoardPost
import com.erionna.eternalreturninfo.ui.activity.BoardSearch
import com.erionna.eternalreturninfo.ui.adapter.BoardRecyclerViewAdapter
import com.erionna.eternalreturninfo.ui.viewmodel.BoardListViewModel
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

    private val listAdapter by lazy {
        BoardRecyclerViewAdapter()
    }

    private val boardViewModel: BoardListViewModel by activityViewModels()

    private val addBoardLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val board = result.data?.getParcelableExtra<BoardModel>("board")

                if (board != null) {
                    boardViewModel.addBoard(board)
                }
            }
        }


    private val loadBoardLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                val updateBoard = result.data?.getParcelableExtra<BoardModel>("updateBoard")

                if(updateBoard != null){
                    boardViewModel.updateBoard(updateBoard)
                }

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
        initModel()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initView() = with(binding) {
        boardRv.adapter = listAdapter
        boardRv.layoutManager = LinearLayoutManager(requireContext())

        listAdapter.setOnItemClickListener(object : BoardRecyclerViewAdapter.OnItemClickListener{
            override fun onItemClick(boardItem: BoardModel) {

                FBRef.postRef.child(boardItem.id).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            val intent = Intent(requireContext(), BoardDeleted::class.java)
                            startActivity(intent)
                        } else {
                            val views = boardItem.views + 1
                            FBRef.postRef.child(boardItem.id).child("views").setValue(views)
                            val intent = Intent(requireContext(), BoardPost::class.java)
                            intent.putExtra("ID", boardItem.id)
                            loadBoardLauncher.launch(intent)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // 데이터 읽기 실패 처리
                    }
                })

            }
        })

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
    private fun initModel() = with(boardViewModel) {
        boardList.observe(viewLifecycleOwner) {
            listAdapter.submitList(
                it.toMutableList().reversed()
            )
        }
    }
}