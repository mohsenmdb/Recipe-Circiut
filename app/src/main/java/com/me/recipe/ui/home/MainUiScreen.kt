package com.me.recipe.ui.home

import androidx.compose.runtime.Stable
import com.me.recipe.domain.features.recipe.model.CategoryRecipe
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.shared.utils.FoodCategory
import com.me.recipe.ui.component.util.GenericDialogInfo
import com.me.recipe.ui.component.util.UiMessage
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainUiScreen(val title: String? = null) : Screen

typealias MainUiSink = (MainUiEvent) -> Unit

@Stable
data class MainUiState(
    val sliderRecipes: ImmutableList<Recipe>? = null,
    val categoriesRecipes: ImmutableList<CategoryRecipe>? = null,
    val errors: GenericDialogInfo? = null,
    val sliderLoading: Boolean = sliderRecipes == null,
    val categoriesLoading: Boolean = categoriesRecipes.isNullOrEmpty(),
    val isDark: Boolean = false,
    val message: UiMessage? = null,
    val eventSink: MainUiSink,
) : CircuitUiState {
    companion object {
        fun testData() = MainUiState(
            sliderRecipes = persistentListOf(Recipe.testData()),
            categoriesRecipes = persistentListOf(CategoryRecipe.testData(), CategoryRecipe.testData()),
            eventSink = {},
        )
    }
}

sealed interface MainUiEvent : CircuitUiEvent {

    data class OnRecipeClicked(val recipe: Recipe) : MainUiEvent
    data class OnCategoryClicked(val category: FoodCategory) : MainUiEvent
    data class OnRecipeLongClick(val title: String) : MainUiEvent
    data object ToggleDarkTheme : MainUiEvent
    data object ClearMessage : MainUiEvent
}

val MainUiState.showShimmer: Boolean
    get() = categoriesLoading
