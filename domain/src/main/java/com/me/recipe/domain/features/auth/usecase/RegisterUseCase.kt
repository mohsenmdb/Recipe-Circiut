package com.me.recipe.domain.features.auth.usecase

import com.me.recipe.domain.features.auth.repository.AuthRepository
import com.me.recipe.domain.features.model.Register
import com.me.recipe.shared.utils.Result
import com.me.recipe.shared.utils.runAsResult
import javax.inject.Inject

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
