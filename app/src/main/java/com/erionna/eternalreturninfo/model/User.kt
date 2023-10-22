package com.erionna.eternalreturninfo.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var server: String? = "",
    var gender: String? = "",
    var tier: String? = "",
    var most: String? = "",
    var uid: String? = ""
){
    @Exclude
    fun toMap():Map<String,Any?>{
        return mapOf(
            "server" to server,
            "gender" to gender,
            "tier" to tier,
            "most" to most,
            "uid" to uid
        )
    }
}