package com.me.recipe.domain.features.recipe.usecases

import android.net.Uri
import com.me.recipe.domain.features.recipe.repository.AddRecipeRepository
import recipe.app.core.Result
import javax.inject.Inject
import recipe.app.core.runAsResult

class AddRecipeUseCase @Inject constructor(
    private val repository: AddRecipeRepository,
) {
    data class Params(
        val title: String,
        val description: String,
        val ingredients: String,
        val imageUri: Uri,
    )

    suspend operator fun invoke(params: Params): Result<Unit> {
        return runAsResult {
            repository.addRecipe(
                title = params.title,
                description = params.description,
                ingredients = params.ingredients,
                imageUri = params.imageUri,
            )
        }
    }
}
