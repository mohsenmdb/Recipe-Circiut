package com.me.recipe.network.features.recipe.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeSearchDto(
    @SerialName(value = "data") val data: Data,
)

@Serializable
data class Data(
    @SerialName(value = "recipes") val results: List<RecipeDto>,
)
