package com.me.recipe.network.features.recipe.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeDto(
    @Json(name = "id") var id: Int?,
    @Json(name = "title") var title: String?,
    @Json(name = "publisher") var publisher: String?,
    @Json(name = "image") var image: String?,
    @Json(name = "rating") var rating: String = "0",
    @Json(name = "ingredients") var ingredients: String?,
    @Json(name = "long_date_updated") var dateUpdatedTimeStamp: Long?,
    @Json(name = "date_updated") var dateUpdated: String?,
)
