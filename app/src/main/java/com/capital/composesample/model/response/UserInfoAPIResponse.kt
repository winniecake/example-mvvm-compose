package com.capital.composesample.model.response

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class UserInfoAPIResponse(
    val code: String?,
    val data: UserData?
)

@Keep
@JsonClass(generateAdapter = true)
data class UserData(
    val id: String?,
    val name: String?,
    val gender: String?,
    val birthday: String?,
    val email: String?,
    val phone: String?
)