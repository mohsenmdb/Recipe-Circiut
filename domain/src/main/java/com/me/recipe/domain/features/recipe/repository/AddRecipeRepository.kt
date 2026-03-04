package com.me.recipe.domain.features.recipe.repository

import android.net.Uri

interface AddRecipeRepository {
    suspend fun addRecipe(
        title: String,
        description: String,
        ingredients: String,
        imageUri: Uri,
    )
}
