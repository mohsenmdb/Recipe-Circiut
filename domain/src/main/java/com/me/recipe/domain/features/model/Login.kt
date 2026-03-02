package com.me.recipe.domain.features.model

import androidx.compose.runtime.Immutable

@Immutable
data class Login(
    val accessToken: String,
    val user: User,
) {
    companion object {
        val EMPTY = Login(
            accessToken = "",
            user = User.EMPTY,
        )

        fun testData() = Login(
            accessToken = "accessToken",
            user = User.testData(),
        )
    }
}
