package com.me.recipe.network.features.recipe.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeSearchResponse(
    @Json(name = "data") var data: Data,
)
@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "recipes") var results: List<RecipeDto>,
)
