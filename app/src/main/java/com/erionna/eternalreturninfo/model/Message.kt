package com.erionna.eternalreturninfo.model

data class Message(
    var message: String?,
    var sendId: String?
) {
    constructor():this("","")
}
