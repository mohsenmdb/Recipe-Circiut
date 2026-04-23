package com.me.recipe.ui.auth

import com.me.recipe.domain.features.auth.model.Login
import com.me.recipe.domain.features.auth.model.Register
import com.me.recipe.domain.features.auth.repository.AuthRepository

class AuthRepositoryFake(
    private val loginResult: Result<Login> = Result.success(Login.EMPTY),
    private val registerResult: Result<Register> = Result.success(Register.EMPTY),
) : AuthRepository {

    override suspend fun login(username: String, password: String): Login {
        return loginResult.getOrThrow()
    }

    override suspend fun register(
        username: String,
        firstName: String,
        lastName: String,
        age: Int?,
        password: String,
    ): Register {
        return registerResult.getOrThrow()
    }
}
