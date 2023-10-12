package com.erionna.eternalreturninfo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ERModel (
    val id: Long? = null,
    val userName: String,
    val msg: String,
    val profilePicture: Int
) : Parcelable