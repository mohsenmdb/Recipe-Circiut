package com.me.recipe.domain.features.recipelist.usecases

import com.me.recipe.domain.features.recipe.model.CategoryRecipe
import com.me.recipe.domain.features.recipelist.repository.RecipeListRepository
import com.me.recipe.domain.util.SubjectInteractor
import com.me.recipe.shared.data.DataState
import com.me.recipe.shared.utils.FoodCategory
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber


class CategoriesRecipesUsecase @Inject constructor(
    private val recipeListRepository: RecipeListRepository,
) : SubjectInteractor<CategoriesRecipesUsecase.Params, Result<ImmutableList<CategoryRecipe>>>() {
    data class Params(
        val categories: ImmutableList<FoodCategory>
    )
    override fun createObservable(params: Params): Flow<Result<ImmutableList<CategoryRecipe>>> {
        return with(params) {
            recipeListRepository.categoriesRecipes(categories)
                .map(Result.Companion::success)
                .catch {
                    emit(Result.failure(it))
                }
        }
    }
}
