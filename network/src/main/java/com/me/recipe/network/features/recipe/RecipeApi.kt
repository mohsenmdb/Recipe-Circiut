package com.me.recipe.network.features.recipe

import com.me.recipe.network.features.recipe.model.CategoriesDto
import com.me.recipe.network.features.recipe.model.RecipeDto
import com.me.recipe.network.features.recipe.model.RecipeSearchDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {

    @GET("recipes")
    suspend fun search(
        @Query("page") page: Int,
        @Query("pageSize") size: Int,
        @Query("query") query: String,
    ): RecipeSearchDto

    @GET("recipes/me")
    suspend fun mine(
        @Query("page") page: Int,
        @Query("pageSize") size: Int,
    ): RecipeSearchDto

    @GET("recipes/one/{id}")
    suspend fun get(
        @Path("id") id: Int,
    ): RecipeDto

    @GET("recipes/categories")
    suspend fun categories(): CategoriesDto?

    @Multipart
    @POST("recipes")
    suspend fun addRecipe(
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("ingredients") ingredients: RequestBody,
        @Part image: MultipartBody.Part,
    )
}
