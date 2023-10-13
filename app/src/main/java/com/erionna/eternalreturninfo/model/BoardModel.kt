package com.erionna.eternalreturninfo.model

import org.w3c.dom.Comment

data class BoardModel (
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val author: String = "",
    val date: String = "",
    val comment: MutableList<CommentModel> = mutableListOf()
)
data class CommentModel(
    val author: String = "",
    val content: String = "",
    val date: String = "",
)