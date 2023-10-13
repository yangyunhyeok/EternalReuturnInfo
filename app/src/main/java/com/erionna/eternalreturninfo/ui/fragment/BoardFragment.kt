package com.erionna.eternalreturninfo.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.erionna.eternalreturninfo.databinding.BoardFragmentBinding
import com.erionna.eternalreturninfo.model.BoardModel
import com.erionna.eternalreturninfo.model.CommentModel
import com.erionna.eternalreturninfo.retrofit.FBRef
import com.erionna.eternalreturninfo.ui.activity.BoardAdd
import com.erionna.eternalreturninfo.ui.activity.BoardPost
import com.erionna.eternalreturninfo.ui.adapter.BoardRecyclerViewAdapter
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

    private val boardList = mutableListOf<BoardModel>()

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

        FBRef.postRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                boardList.clear()

                for(data in snapshot.children){
                    val board = data.getValue<BoardModel>()
                    if (board != null) {
                        boardList.add(board)
                    }
                }

                boardList.reverse()
                listAdapter.submitList(boardList)
                listAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        listAdapter.setOnItemClickListener(object : BoardRecyclerViewAdapter.OnItemClickListener{
            override fun onItemClick(boardItem: BoardModel) {
                val intent = Intent(requireContext(), BoardPost::class.java)
                intent.putExtra("ID", boardItem.id)
                startActivity(intent)
            }
        })




        boardFab.setOnClickListener {
            val intent = Intent(requireContext(), BoardAdd::class.java)
            startActivity(intent)
        }
    }

}