package com.capital.composesample.model.response

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class ContactsAPIResponse(
    val code: String?,
    val data: List<ContactsData>?,
)

@Keep
@JsonClass(generateAdapter = true)
data class ContactsData(
    val id: Int?,
    val name: String?,
    val gender: String?,
    val email: String?,
    val phone: String?
)