package com.me.recipe.network.features.recipe.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginDto(
    @SerialName(value = "data") val data: Data?,
) {

    @Serializable
    data class Data(
        @SerialName(value = "accessToken") val accessToken: String?,
    )
}
