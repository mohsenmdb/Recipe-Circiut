package com.me.recipe.domain.features.recipe.repository

import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.shared.data.DataState
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
     fun getRecipe(
        recipeId: Int,
        uid: String,
    ): Flow<Recipe>
}
