package com.erionna.eternalreturninfo.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.erionna.eternalreturninfo.R
import com.erionna.eternalreturninfo.databinding.BoardPostActivityBinding
import com.erionna.eternalreturninfo.databinding.BoardSearchActivityBinding
import com.erionna.eternalreturninfo.model.BoardModel
import com.erionna.eternalreturninfo.retrofit.FBRef
import com.erionna.eternalreturninfo.ui.adapter.BoardRecyclerViewAdapter
import com.erionna.eternalreturninfo.ui.viewmodel.BoardListViewModel
import com.erionna.eternalreturninfo.ui.viewmodel.BoardListViewModelFactory
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class BoardSearch : AppCompatActivity() {

    private lateinit var binding: BoardSearchActivityBinding

    private val boardViewModel by lazy {
        ViewModelProvider(this, BoardListViewModelFactory()).get(BoardListViewModel::class.java)
    }

    private val listAdapter by lazy {
        BoardRecyclerViewAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BoardSearchActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initModel()
    }

    private fun initView() = with(binding) {

        boardSearchRv.adapter = listAdapter
        boardSearchRv.layoutManager = LinearLayoutManager(this@BoardSearch)

        boardSearchIbBack.setOnClickListener {
            finish()
        }

        boardSearchIbClear.setOnClickListener {
            boardSearchEtSearch.setText("")
        }

        boardSearchEtSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event.keyCode == KeyEvent.KEYCODE_ENTER) {
                if (event.action == KeyEvent.ACTION_DOWN) {

                    boardViewModel.clearSearchBoard()

                    val searchText = boardSearchEtSearch.text.toString()
                    val query = FBRef.postRef.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff")

                    query.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            boardSearchTvPostCount.visibility = View.VISIBLE
                            boardSearchTvPostCount.text = dataSnapshot.childrenCount.toString() + " Post"

                            if(dataSnapshot.exists()){
                                boardSearchTvResult.visibility = View.INVISIBLE

                                for (snapshot in dataSnapshot.children) {
                                    val searchBoard = snapshot.getValue<BoardModel>()
                                    if (searchBoard != null) {
                                        boardViewModel.addSearchBoard(searchBoard)
                                    }
                                }
                            }else{
                                boardSearchTvResult.visibility = View.VISIBLE
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // 검색이 실패한 경우
                        }
                    })
                }
                true
            } else {
                false
            }
        }

        listAdapter.setOnItemClickListener(object : BoardRecyclerViewAdapter.OnItemClickListener{
            override fun onItemClick(boardItem: BoardModel) {

                FBRef.postRef.child(boardItem.id).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            val intent = Intent(this@BoardSearch, BoardDeleted::class.java)
                            startActivity(intent)
                        } else {
                            val views = boardItem.views + 1
                            FBRef.postRef.child(boardItem.id).child("views").setValue(views)
                            val intent = Intent(this@BoardSearch, BoardPost::class.java)
                            intent.putExtra("ID", boardItem.id)
                            startActivity(intent)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // 데이터 읽기 실패 처리
                    }
                })
            }
        })

    }

    private fun initModel() = with(binding) {
        boardViewModel.searchBoardList.observe(this@BoardSearch){ searchBoardList ->
            val newBoardList = searchBoardList.reversed()
            listAdapter.submitList(newBoardList.toMutableList())
        }
    }
}