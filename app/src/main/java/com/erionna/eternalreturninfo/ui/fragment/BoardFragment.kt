package com.erionna.eternalreturninfo.ui.fragment

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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.erionna.eternalreturninfo.databinding.BoardFragmentBinding
import com.erionna.eternalreturninfo.model.BoardModel
import com.erionna.eternalreturninfo.retrofit.FBRef
import com.erionna.eternalreturninfo.ui.activity.BoardAdd
import com.erionna.eternalreturninfo.ui.activity.BoardPost
import com.erionna.eternalreturninfo.ui.adapter.BoardRecyclerViewAdapter
import com.erionna.eternalreturninfo.ui.viewmodel.BoardListViewModel
import com.erionna.eternalreturninfo.ui.viewmodel.BoardListViewModelFactory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

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

    private val addTodoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val board = result.data?.getParcelableExtra<BoardModel>("board")

                if (board != null) {
                    boardViewModel.addBoard(board)
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
                val intent = Intent(requireContext(), BoardPost::class.java)
                intent.putExtra("ID", boardItem.id)
                startActivity(intent)
            }
        })

        boardFab.setOnClickListener {
            val intent = Intent(requireContext(), BoardAdd::class.java)
            addTodoLauncher.launch(intent)
        }
    }

    private fun initModel() = with(binding) {
        boardViewModel.boardList.observe(viewLifecycleOwner){ boardList ->
            val newBoardList = boardList.reversed()
            listAdapter.submitList(newBoardList.toMutableList())
        }
    }

}