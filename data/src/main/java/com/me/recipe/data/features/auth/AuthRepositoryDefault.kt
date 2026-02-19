package com.me.recipe.data.features.auth

import com.me.recipe.data.features.recipe.mapper.LoginDtoMapper
import com.me.recipe.domain.features.auth.repository.AuthRepository
import com.me.recipe.domain.features.model.Login
import com.me.recipe.network.features.recipe.AuthApi
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class AuthRepositoryDefault @Inject constructor(
    private val authApi: AuthApi,
    private val loginDtoMapper: LoginDtoMapper,
) : AuthRepository {

    override suspend fun login(email: String, password: String): Login {
        val response = authApi.login(email, password)
        return loginDtoMapper.map(response)
    }

    override fun register(
        email: String,
        password: String,
    ): Flow<Login> {
        TODO("Not yet implemented")
    }
}
