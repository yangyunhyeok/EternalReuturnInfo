package com.erionna.eternalreturninfo.model

data class BoardModel (
    val title: String,
    val content: String,
    val user: String,
    val date: String,
    val comment: Comment,
    val commentSize: Int
)

data class Comment(
    val user: String,
    val content: String,
    val date: String,
)