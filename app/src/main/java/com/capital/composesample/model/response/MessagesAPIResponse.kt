package com.capital.composesample.model.response

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class MessagesAPIResponse(
    val code: String?,
    val data: List<MessagesData>?,
)

@Keep
@JsonClass(generateAdapter = true)
data class MessagesData(
    var title: String?,
    var content: String?,
    var time: String?
)