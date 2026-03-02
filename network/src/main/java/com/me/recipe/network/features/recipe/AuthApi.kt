package com.me.recipe.network.features.recipe

import com.me.recipe.network.features.recipe.model.LoginDto
import com.me.recipe.network.features.recipe.model.RegisterDto
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthApi {
    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
    ): LoginDto

    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("first_name") firstName: String,
        @Field("last_name") lastName: String,
        @Field("age") age: Int?,
        @Field("password") password: String,
    ): RegisterDto
}
