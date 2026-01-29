package com.me.recipe.domain.features.recipe.model

import androidx.compose.runtime.Immutable
import com.me.recipe.shared.utils.CategoryRowType
import com.me.recipe.shared.utils.FoodCategory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class CategoryRecipe(
    val rowType: CategoryRowType,
    val category: FoodCategory,
    val recipes: ImmutableList<Recipe>,
) {
    companion object {
        fun testData(category: FoodCategory = FoodCategory.CHICKEN) = CategoryRecipe(
            rowType = CategoryRowType.ROW,
            category = category,
            recipes = persistentListOf(Recipe.testData()),
        )
    }
}
