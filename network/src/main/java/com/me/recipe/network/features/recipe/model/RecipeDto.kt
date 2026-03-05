package com.me.recipe.network.features.recipe.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipeDto(
    @SerialName(value = "id") val id: Int?,
    @SerialName(value = "title") val title: String?,
    @SerialName(value = "description") val description: String?,
    @SerialName(value = "user") val publisher: UserDto?,
    @SerialName(value = "image") val image: String?,
    @SerialName(value = "rating") val rating: String = "0",
    @SerialName(value = "ingredients") val ingredients: String?,
    @SerialName(value = "createdAt") val createdAt: Long?,
    @SerialName(value = "updatedAt") val updatedAt: Long?,
)
