package com.erionna.eternalreturninfo.retrofit

import com.erionna.eternalreturninfo.model.BoardModel
import com.erionna.eternalreturninfo.model.CommentModel
import com.erionna.eternalreturninfo.model.UserModel

object BoardSingletone {

    private val boardList: MutableList<BoardModel> = mutableListOf()
    private val commentList: MutableList<CommentModel> = mutableListOf()


    private val userList: MutableList<UserModel> = mutableListOf(
        UserModel("user1", "https://mblogthumb-phinf.pstatic.net/MjAyMTAxMDZfMTcw/MDAxNjA5OTE2NjQ1NzM5.7KFfWIWTn0HQgLpwypBfk5OCsMuDNC_8dNAsRSBInpsg.3go5PasEPOgh9gwi71GuDB40b_yCOsjDYyTo5TjEdNMg.JPEG.miyampuzzy/EkDjSpXVgAIn_fJ.jpg?type=w800"),
        UserModel("user2", "https://blog.kakaocdn.net/dn/cbiho5/btrqLUBuZ8T/QweFCSHv78KjZEG7lpx0Nk/img.jpg")
    )

    fun LoginUser(): UserModel {
        return userList[0]
    }

    fun anotherUser(): UserModel {
        return userList[1]
    }

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