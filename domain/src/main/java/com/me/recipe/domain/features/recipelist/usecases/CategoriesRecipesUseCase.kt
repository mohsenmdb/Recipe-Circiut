package com.me.recipe.domain.features.recipelist.usecases

import com.me.recipe.domain.features.recipe.model.CategoryRecipe
import com.me.recipe.domain.features.recipelist.repository.CategoriesRepository
import com.me.recipe.domain.util.SubjectInteractor
import com.me.recipe.shared.utils.FoodCategory
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class CategoriesRecipesUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
) : SubjectInteractor<CategoriesRecipesUseCase.Params, Result<ImmutableList<CategoryRecipe>>>() {
    data class Params(
        val isOffline: Boolean = false,
    )
    override fun createObservable(params: Params): Flow<Result<ImmutableList<CategoryRecipe>>> {
        val categories = if (params.isOffline) categoriesRepository.categoriesRecipesOffline() else categoriesRepository.categoriesRecipes()
        return categories
            .map(Result.Companion::success)
            .catch {
                emit(Result.failure(it))
            }
    }
}
