package com.me.recipe.domain.features.model

import androidx.compose.runtime.Immutable

@Immutable
data class Login(
    val accessToken: String,
) {
    companion object {
        val EMPTY = Login(
            accessToken = "",
        )

        fun testData() = Login(
            accessToken = "accessToken",
        )
    }
}
