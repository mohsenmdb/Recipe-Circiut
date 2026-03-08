package com.me.recipe.network.features.recipe.model

import kotlinx.serialization.Serializable

@Serializable
data class ServerErrorDto(
    val success: Boolean? = null,
    val statusCode: Int? = null,
    val message: String? = null,
    val data: Unit? = null,
    val errors: List<String>? = null,
)
