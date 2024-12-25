package com.me.recipe.domain.features.recipelist.usecases

import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipelist.repository.RecipeListRepository
import com.me.recipe.domain.util.SubjectInteractor
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class SliderRecipesUsecase @Inject constructor(
    private val recipeListRepository: RecipeListRepository,
) : SubjectInteractor<Unit, Result<ImmutableList<Recipe>>>() {
    override fun createObservable(params: Unit): Flow<Result<ImmutableList<Recipe>>> {
        return recipeListRepository.slider()
            .map(Result.Companion::success)
            .catch {
                emit(Result.failure(it))
            }
    }
}
