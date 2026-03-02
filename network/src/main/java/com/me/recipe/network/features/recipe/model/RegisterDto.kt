package com.me.recipe.network.features.recipe.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegisterDto(
    @SerialName(value = "data") val data: Data?,
) {

    @Serializable
    data class Data(
        @SerialName(value = "accessToken") val accessToken: String?,
        @SerialName(value = "user") val user: UserDto?,
    )
}

@Serializable
data class UserDto(
    @SerialName(value = "id") val id: Int?,
    @SerialName(value = "username") val username: String?,
    @SerialName(value = "first_name") val firstName: String?,
    @SerialName(value = "last_name") val lastName: String?,
    @SerialName(value = "age") val age: Int?,
)
