package com.me.recipe.domain.features.auth.repository

import com.me.recipe.domain.features.model.Login
import com.me.recipe.domain.features.model.Register
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(username: String, password: String): Login

    suspend fun register(
        username: String,
        firstName: String,
        lastName: String,
        age: Int?,
        password: String,
    ): Register
}
