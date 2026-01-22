package com.me.recipe.network.features.recipe

import com.me.recipe.network.features.recipe.model.CategoriesDto
import com.me.recipe.network.features.recipe.model.RecipeDto
import com.me.recipe.network.features.recipe.model.RecipeSearchDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {

    @GET("recipes")
    suspend fun search(
        @Query("page") page: Int,
        @Query("pageSize") size: Int,
        @Query("query") query: String,
    ): RecipeSearchDto

    @GET("recipes/one/{id}")
    suspend fun get(
        @Path("id") id: Int,
    ): RecipeDto

    @GET("recipes/categories")
    suspend fun categories(): CategoriesDto?
}
