package com.me.recipe.domain.features.recipe.usecases

import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipe.repository.RecipeRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class GetTodayRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
) {
    operator fun invoke(): Flow<Result<Recipe>> {
        return recipeRepository.getTodayRecipe()
            .map(Result.Companion::success)
            .catch {
                emit(Result.failure(it))
            }
    }
}
