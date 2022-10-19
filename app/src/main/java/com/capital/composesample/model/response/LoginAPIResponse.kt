package com.capital.composesample.model.response

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class LoginAPIResponse(
    val code: String?,
    val msg: String?,
    val data: LoginData?
)

@Keep
@JsonClass(generateAdapter = true)
data class LoginData(
    val id: String?,
    val token: String?
)