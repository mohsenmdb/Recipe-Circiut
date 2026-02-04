package com.me.recipe.network.features.recipe.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeDto(
    @Json(name = "id") val id: Int?,
    @Json(name = "title") val title: String?,
    @Json(name = "user_name") val publisher: String?,
    @Json(name = "image") val image: String?,
    @Json(name = "rating") val rating: String = "0",
    @Json(name = "ingredients") val ingredients: String?,
    @Json(name = "long_date_updated") val dateUpdatedTimeStamp: Long?,
    @Json(name = "updatedAt") val dateUpdated: String?,
)
