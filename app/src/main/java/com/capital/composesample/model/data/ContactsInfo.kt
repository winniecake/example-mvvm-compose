package com.capital.composesample.model.data

data class ContactsInfo(
    var id: Int?,
    var name: String?,
    var gender: String?,
    var email: String?,
    var phone: String?
){
    constructor(): this(0,
        "",
        "",
        "",
        "")
}