package com.me.recipe.network.features.recipe.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeSearchDto(
    @Json(name = "data") val data: Data,
)
@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "recipes") val results: List<RecipeDto>,
)
