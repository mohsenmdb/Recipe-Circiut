package com.me.recipe.domain.features.model

import androidx.compose.runtime.Immutable

@Immutable
data class User(
    val username: String,
    val firstName: String,
    val lastName: String,
    val age: Int?,
) {
    companion object {
        val EMPTY = User(
            username = "",
            firstName = "",
            lastName = "",
            age = null,
        )

        fun testData() = User(
            username = "user name",
            firstName = "first name",
            lastName = "last name",
            age = 11,
        )
    }
}
