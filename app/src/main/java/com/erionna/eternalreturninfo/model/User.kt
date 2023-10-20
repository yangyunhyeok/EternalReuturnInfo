package com.erionna.eternalreturninfo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class User(
    val id: Long? = null,
    var email: String? = null,
    var password: String? = null,
    var name: String? = null,
    var uId: String? = null
)
