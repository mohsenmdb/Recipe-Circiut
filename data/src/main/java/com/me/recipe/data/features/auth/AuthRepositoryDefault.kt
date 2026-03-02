package com.me.recipe.data.features.auth

import com.me.recipe.data.features.recipe.mapper.LoginDtoMapper
import com.me.recipe.data.features.recipe.mapper.RegisterDtoMapper
import com.me.recipe.domain.features.auth.repository.AuthRepository
import com.me.recipe.domain.features.model.Login
import com.me.recipe.domain.features.model.Register
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
