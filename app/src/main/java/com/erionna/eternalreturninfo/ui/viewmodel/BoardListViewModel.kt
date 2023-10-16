package com.erionna.eternalreturninfo.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.erionna.eternalreturninfo.model.BoardModel
import com.erionna.eternalreturninfo.model.CommentModel
import com.erionna.eternalreturninfo.retrofit.FBRef
import com.erionna.eternalreturninfo.ui.activity.MainActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class BoardListViewModel() : ViewModel(){

    private val _boardList = MutableLiveData<MutableList<BoardModel>>()
    val boardList: LiveData<MutableList<BoardModel>>
        get() = _boardList

    private val _commentList = MutableLiveData<MutableList<CommentModel>>()
    val commentList: LiveData<MutableList<CommentModel>>
        get() = _commentList


    init {
        FBRef.postRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val unsortedBoardList = mutableListOf<BoardModel>()
                for (data in snapshot.children) {
                    val board = data.getValue<BoardModel>()
                    if (board != null) {
                        unsortedBoardList.add(board)
                    }
                }
                _boardList.value = unsortedBoardList
            }

            override fun onCancelled(error: DatabaseError) {
                // 에러 처리
            }
        })
    }

    fun addBoard(item: BoardModel) {
        if (item == null) {
            return
        }

        val currentList = boardList.value.orEmpty().toMutableList()
        currentList.add(item)
        _boardList.value = currentList
    }

    fun initComment(items: MutableList<CommentModel>) {
        if (items == null) {
            return
        }

        val currentList = commentList.value.orEmpty().toMutableList()
        currentList.addAll(items)
        _commentList.value = currentList
    }

    fun addComment(item: CommentModel) {
        if (item == null) {
            return
        }

        val currentList = commentList.value.orEmpty().toMutableList()
        currentList.add(item)
        _commentList.value = currentList
    }

}

class BoardListViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BoardListViewModel::class.java)) {
            return BoardListViewModel() as T
        } else {
            throw IllegalArgumentException("Not found ViewModel class.")
        }
    }
}
