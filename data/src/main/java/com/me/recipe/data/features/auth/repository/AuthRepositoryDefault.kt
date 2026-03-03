package com.me.recipe.data.features.auth.repository

import com.me.recipe.data.features.auth.mapper.LoginDtoMapper
import com.me.recipe.data.features.auth.mapper.RegisterDtoMapper
import com.me.recipe.domain.features.auth.model.Login
import com.me.recipe.domain.features.auth.model.Register
import com.me.recipe.domain.features.auth.repository.AuthRepository
import com.me.recipe.network.features.recipe.AuthApi
import javax.inject.Inject

class AuthRepositoryDefault @Inject constructor(
    private val authApi: AuthApi,
    private val loginDtoMapper: LoginDtoMapper,
    private val registerDtoMapper: RegisterDtoMapper,
) : AuthRepository {

    override suspend fun login(username: String, password: String): Login {
        val response = authApi.login(username, password)
        return loginDtoMapper.map(response)
    }

    override suspend fun register(
        username: String,
        firstName: String,
        lastName: String,
        age: Int?,
        password: String,
    ): Register {
        val response = authApi.register(username, firstName, lastName, age, password)
        return registerDtoMapper.map(response)
    }
}
