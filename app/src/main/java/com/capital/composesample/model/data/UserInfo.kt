package com.capital.composesample.model.data

data class UserInfo(
    var id: String?,
    var name: String?,
    var gender: String?,
    var birthday: String?,
    var email: String?,
    var phone: String?
){
   constructor(): this("",
       "",
       "",
       "",
       "",
       "")
}