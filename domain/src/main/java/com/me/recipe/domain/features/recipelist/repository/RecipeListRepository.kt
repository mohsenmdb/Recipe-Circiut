package com.me.recipe.domain.features.recipelist.repository

import com.me.recipe.domain.features.recipe.model.CategoryRecipe
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.shared.data.DataState
import com.me.recipe.shared.utils.FoodCategory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface RecipeListRepository {
    fun search(page: Int, query: String, size: Int): Flow<ImmutableList<Recipe>>
    fun restore(page: Int, query: String): Flow<ImmutableList<Recipe>>
    fun categoriesRecipes(categories: ImmutableList<FoodCategory>): Flow<ImmutableList<CategoryRecipe>>
    fun slider(): Flow<ImmutableList<Recipe>>
    suspend fun getTopRecipe(): Recipe
}
