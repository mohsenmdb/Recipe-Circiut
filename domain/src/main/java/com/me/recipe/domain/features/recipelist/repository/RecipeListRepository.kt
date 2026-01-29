package com.me.recipe.domain.features.recipelist.repository

import com.me.recipe.domain.features.recipe.model.Recipe
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface RecipeListRepository {
    fun search(page: Int, query: String, size: Int): Flow<ImmutableList<Recipe>>
    fun restore(page: Int, query: String): Flow<ImmutableList<Recipe>>
    suspend fun getTopRecipe(): Recipe
}
