package com.github.ianellis.clean.data.catfacts

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CatFactEntity(
    @Json(name = "status")
    val status: Status,
    @Json(name = "type")
    val type: String,
    @Json(name = "deleted")
    val deleted: Boolean,
    @Json(name = "_id")
    val id: String,
    @Json(name = "user")
    val user: String,
    @Json(name = "text")
    val text: String,
    @Json(name = "source")
    val source: String,
    @Json(name = "updatedAt")
    val updatedAt: String,
    @Json(name = "createdAt")
    val createdAt: String,
    @Json(name = "used")
    val used: Boolean
) {
    @JsonClass(generateAdapter = true)
    data class Status(
        @Json(name = "verified")
        val verified: Boolean,
        @Json(name = "sentCount")
        val sentCount: Int,
        @Json(name = "feedback")
        val feedback: String?
    )
}
