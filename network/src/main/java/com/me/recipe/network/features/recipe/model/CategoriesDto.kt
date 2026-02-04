package com.me.recipe.network.features.recipe.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoriesDto(
    @SerialName(value = "data") val categories: List<CategoriesListDto>?,
)

@Serializable
data class CategoriesListDto(
    @SerialName(value = "type") val type: RowTypeDto?,
    @SerialName(value = "category") val categoryName: FoodCategoryDto?,
    @SerialName(value = "recipes") val recipes: List<RecipeDto>?,
)

@Serializable
enum class RowTypeDto {
    SLIDER,
    ROW,
}

@Serializable
enum class FoodCategoryDto {
    CHICKEN,
    SOUP,
    DESSERT,
    VEGETARIAN,
    MILK,
    VEGAN,
    PIZZA,
    DONUT,
    WATER,
    BEEF,
    PASTA,
    ALL,
}
