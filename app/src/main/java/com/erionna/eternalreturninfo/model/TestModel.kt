package com.erionna.eternalreturninfo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TestModel (
    val userName: String
) : Parcelable