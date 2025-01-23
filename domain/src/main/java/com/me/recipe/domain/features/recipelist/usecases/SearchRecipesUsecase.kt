package com.me.recipe.domain.features.recipelist.usecases

import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipelist.repository.RecipeListRepository
import com.me.recipe.domain.util.ForceFresh
import com.me.recipe.domain.util.SubjectInteractor
import com.me.recipe.shared.utils.RECIPE_PAGINATION_FIRST_PAGE
import com.me.recipe.shared.utils.RECIPE_PAGINATION_PAGE_SIZE
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class SearchRecipesUsecase @Inject constructor(
    private val recipeListRepository: RecipeListRepository,
) : SubjectInteractor<SearchRecipesUsecase.Params, Result<ImmutableList<Recipe>>>() {
    data class Params(
        val page: Int = RECIPE_PAGINATION_FIRST_PAGE,
        val query: String = "",
        val size: Int = RECIPE_PAGINATION_PAGE_SIZE,
        val refresher: ForceFresh? = null,

    )
    override fun createObservable(params: Params): Flow<Result<ImmutableList<Recipe>>> {
        return recipeListRepository.search(params.page, params.query, params.size)
            .map(Result.Companion::success)
            .catch {
                emit(Result.failure(it))
            }
    }
}
