package com.erionna.eternalreturninfo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ERModel (
    var server: String? = null,
    var name: String? = null,
    var gender: String? = null,
    var tier: String? = null,
    var most: String? = null,

    val id: Long? = null,
    val email: String? = null,

    val password: String? = null,
    val uid: String? = null,
    var msg: String? = null,
    val profilePicture: String? = null,
    var time: String? = null


) : Parcelable