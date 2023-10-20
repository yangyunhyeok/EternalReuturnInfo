package com.erionna.eternalreturninfo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import org.w3c.dom.Comment
import java.util.Date

@Parcelize
data class BoardModel (
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val author: UserModel? = UserModel("user1", ""),
    val date: Long = 0,
    val comments: Map<String, CommentModel> = mapOf(),
    val views: Int = 0
) : Parcelable

@Parcelize
data class CommentModel(
    val id: String = "",
    val author: UserModel? = UserModel("user1", ""),
    val content: String = "",
    val date: Long = 0
) : Parcelable

@Parcelize
data class UserModel(
    val user: String = "",
    val userImage: String = "",
) : Parcelable