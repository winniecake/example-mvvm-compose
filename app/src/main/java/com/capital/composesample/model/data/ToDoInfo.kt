package com.capital.composesample.model.data


data class ToDoInfo(
    var no: String?,
    var title: String?,
    var content: String?,
    var check: Boolean?
){
    constructor(): this("","", "", false)
}