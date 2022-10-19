package com.capital.composesample.model.data

data class MessagesInfo(
    var title: String?,
    var content: String?,
    var time: String?
){
    constructor(): this("", "", "")
}