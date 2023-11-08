package com.erionna.eternalreturninfo.ui.fragment.board

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.erionna.eternalreturninfo.databinding.BoardRvFragmentBinding
import com.erionna.eternalreturninfo.model.BoardModel
import com.erionna.eternalreturninfo.retrofit.FBRef
import com.erionna.eternalreturninfo.ui.activity.board.BoardDeleted
import com.erionna.eternalreturninfo.ui.activity.board.BoardPost
import com.erionna.eternalreturninfo.ui.adapter.board.BoardRecyclerViewAdapter
import com.erionna.eternalreturninfo.ui.viewmodel.BoardListViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class BoardNoticeFragment : Fragment() {

    companion object {
        fun newInstance() = BoardNoticeFragment()
    }

    private val boardViewModel: BoardListViewModel by activityViewModels()

    private var _binding: BoardRvFragmentBinding? = null
    private val binding get() = _binding!!

    private val listAdapter by lazy {
        BoardRecyclerViewAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BoardRvFragmentBinding.inflate(inflater, container, false)
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

        val loadBoardLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra("updateBoard", BoardModel::class.java)?.let { updateBoard ->
                        boardViewModel.updateBoard(updateBoard)
                    }
                } else {
                    result.data?.getParcelableExtra<BoardModel>("updateBoard")?.let { updateBoard ->
                        boardViewModel.updateBoard(updateBoard)
                    }
                }

            }else{

            }
        }

        boardNoticeRv.adapter = listAdapter
        boardNoticeRv.layoutManager = LinearLayoutManager(requireContext())

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
    }

    private fun initModel() = with(boardViewModel) {
        boardList.observe(viewLifecycleOwner) {

            val noticeList = it.filter { it.category == "공지" }

            listAdapter.submitList(
                noticeList.toMutableList().reversed()
            )
        }
    }

}