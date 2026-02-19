package com.me.recipe.domain.features.auth.repository

import com.me.recipe.domain.features.model.Login
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Login

    fun register(
        email: String,
        password: String,
    ): Flow<Login>
}
