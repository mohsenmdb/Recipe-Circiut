package com.me.recipe.domain.features.auth.usecase

import com.me.recipe.domain.features.auth.model.Login
import com.me.recipe.domain.features.auth.repository.AuthRepository
import com.me.recipe.shared.utils.Result
import com.me.recipe.shared.utils.runAsResult
import javax.inject.Inject

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
