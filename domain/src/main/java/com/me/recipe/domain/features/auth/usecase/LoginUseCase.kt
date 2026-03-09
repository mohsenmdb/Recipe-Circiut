package com.me.recipe.domain.features.auth.usecase

import com.me.recipe.domain.features.auth.model.Login
import com.me.recipe.domain.features.auth.repository.AuthRepository
import javax.inject.Inject
import recipe.app.core.Result
import recipe.app.core.runAsResult

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    data class Params(
        val username: String,
        val password: String,
    )
    suspend operator fun invoke(params: Params): Result<Login> {
        return runAsResult {
            repository.login(params.username, params.password)
        }
    }
}
