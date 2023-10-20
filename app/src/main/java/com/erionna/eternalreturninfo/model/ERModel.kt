package com.erionna.eternalreturninfo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ERModel (
    val id: Long? = null,
    val email: String? = null,
    val name: String? = null,
    val password: String? = null,
    val uid: String? = null,
    var msg: String? = null,
    val profilePicture: Int? = null,
    var time: String? = null
) : Parcelable