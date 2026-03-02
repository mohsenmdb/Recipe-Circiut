package com.me.recipe.domain.features.model

import androidx.compose.runtime.Immutable

@Immutable
data class Register(
    val accessToken: String,
    val user: User,
) {
    companion object {
        val EMPTY = Register(
            accessToken = "",
            user = User.EMPTY,
        )

        fun testData() = Register(
            accessToken = "accessToken",
            user = User.testData(),
        )
    }
}
