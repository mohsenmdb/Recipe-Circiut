package com.me.recipe.network.features.recipe.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoriesDto(
    @Json(name = "data") val categories: List<CategoriesListDto>?,
)

@JsonClass(generateAdapter = true)
data class CategoriesListDto(
    @Json(name = "category") val categoryName: FoodCategoryDto?,
    @Json(name = "recipes") val recipes: List<RecipeDto>?,
)

@JsonClass(generateAdapter = false)
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
}
