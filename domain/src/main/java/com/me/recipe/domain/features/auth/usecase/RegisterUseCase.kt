package com.me.recipe.domain.features.auth.usecase

import com.me.recipe.domain.features.auth.model.Register
import com.me.recipe.domain.features.auth.repository.AuthRepository
import javax.inject.Inject
import recipe.app.core.Result
import recipe.app.core.runAsResult

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    data class Params(
        val username: String,
        val firstName: String,
        val lastName: String,
        val age: Int?,
        val password: String,
    )

    suspend operator fun invoke(params: Params): Result<Register> {
        return runAsResult {
            repository.register(
                username = params.username,
                firstName = params.firstName,
                lastName = params.lastName,
                age = params.age,
                password = params.password,
            )
        }
    }
}
