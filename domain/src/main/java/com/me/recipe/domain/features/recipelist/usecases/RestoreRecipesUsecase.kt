package com.me.recipe.domain.features.recipelist.usecases

import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipelist.repository.RecipeListRepository
import com.me.recipe.domain.util.SubjectInteractor
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Restore a list of recipes after process death.
 */

class RestoreRecipesUsecase @Inject constructor(
    private val recipeListRepository: RecipeListRepository,
) : SubjectInteractor<RestoreRecipesUsecase.Params, Result<ImmutableList<Recipe>>>() {
    data class Params(
        val page: Int,
        val query: String,
    )
    override fun createObservable(params: Params): Flow<Result<ImmutableList<Recipe>>> {
        return recipeListRepository.restore(params.page, params.query)
            .map(Result.Companion::success)
            .catch {
                emit(Result.failure(it))
            }
    }
}
