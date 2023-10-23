package com.erionna.eternalreturninfo.model

data class Message(
    var message: String? = null,
    var sendId: String? = null,
    var receiverId: String? = null,
    var time: String? = null,
    var readOrNot: Boolean? = null
) {
    constructor() : this("", "", "", "",null)
}
