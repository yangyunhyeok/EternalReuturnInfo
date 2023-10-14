package com.erionna.eternalreturninfo.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.erionna.eternalreturninfo.R
import com.erionna.eternalreturninfo.model.ERModel
import java.util.concurrent.atomic.AtomicLong

class ChatListViewModel(
    private val idGenerate: AtomicLong
) : ViewModel() {
    private val _list: MutableLiveData<List<ERModel>> = MutableLiveData()
    val list: LiveData<List<ERModel>> get() = _list

    fun addUser(
        item: ERModel?
    ) {
        if (item == null) {
            return
        }

        val currentList = list.value.orEmpty().toMutableList()
        _list.value = currentList.apply {
            add(
                item.copy(
                    id = idGenerate.getAndIncrement()
                )
            )
        }
    }

    fun clearList() {
        val currentList = list.value.orEmpty().toMutableList()
        currentList.clear()
        _list.value = currentList
    }
}

class ChatListViewModelFactory : ViewModelProvider.Factory {
    private val idGenerate = AtomicLong(1L)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatListViewModel::class.java)) {
            return ChatListViewModel(idGenerate) as T
        } else {
            throw IllegalArgumentException("not found correct viewModel")
        }
    }
}