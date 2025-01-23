package com.me.recipe.domain.features.recipe.usecases

import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipe.repository.RecipeRepository
import com.me.recipe.domain.util.SubjectInteractor
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class GetRecipeUsecase @Inject constructor(
    private val recipeRepository: RecipeRepository,
) : SubjectInteractor<GetRecipeUsecase.Params, Result<Recipe>>() {
    data class Params(
        val recipeId: Int,
        val uid: String,
    )
    override fun createObservable(params: Params): Flow<Result<Recipe>> {
        return recipeRepository.getRecipe(params.recipeId, params.uid)
            .map(Result.Companion::success)
            .catch {
                emit(Result.failure(it))
            }
    }
}
