package com.erionna.eternalreturninfo.model

data class ERModel (
    val id: Long? = null,
    val email: String? = null,
    val name: String? = null,
    val password: String? = null,
    val uId: String? = null,
    val msg: String? = null,
    val profilePicture: Int? = null
) {
    constructor() : this(null,"","","","","",null)
}