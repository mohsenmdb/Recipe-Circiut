package com.me.recipe.domain.features.recipelist.repository

import com.me.recipe.domain.features.recipe.model.CategoryRecipe
import com.me.recipe.shared.utils.FoodCategory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface CategoriesRepository {
    fun categoriesRecipes(): Flow<ImmutableList<CategoryRecipe>>
    fun categoriesRecipesOffline(): Flow<ImmutableList<CategoryRecipe>>
}
