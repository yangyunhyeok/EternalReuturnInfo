package com.erionna.eternalreturninfo.retrofit

import com.erionna.eternalreturninfo.model.BoardModel
import com.erionna.eternalreturninfo.model.CommentModel

object BoardSingletone {

    private val boardList: MutableList<BoardModel> = mutableListOf()
    private val commentList: MutableList<CommentModel> = mutableListOf()

    fun addBoard(item: BoardModel){
        boardList.add(item)
    }

    fun initBoard(items: MutableList<BoardModel>){
        boardList.addAll(items)
    }

    fun removeBoard(item: BoardModel){

        fun findIndex(item: BoardModel?): Int {
            val currentList = boardList.orEmpty().toMutableList()

            val findTodo = currentList.find {
                it.id == item?.id
            }
            return currentList.indexOf(findTodo)
        }

        boardList.removeAt(findIndex(item))
    }

    fun removeBoard2(position: Int){
        boardList.removeAt(position)
    }

}