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
    val msg: String? = null,
    val profilePicture: Int? = null,
    val time: String? = null
) : Parcelable