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
data class HomeUiScreen(val title: String? = null) : Screen

typealias HomeUiEventSink = (HomeUiEvent) -> Unit

@Stable
data class HomeUiState(
    val sliderRecipes: ImmutableList<Recipe>? = null,
    val categoriesRecipes: ImmutableList<CategoryRecipe>? = null,
    val errors: GenericDialogInfo? = null,
    val sliderLoading: Boolean = sliderRecipes == null,
    val categoriesLoading: Boolean = categoriesRecipes.isNullOrEmpty(),
    val isDark: Boolean = false,
    val message: UiMessage? = null,
    val eventSink: HomeUiEventSink,
) : CircuitUiState {
    companion object {
        fun testData() = HomeUiState(
            sliderRecipes = persistentListOf(Recipe.testData()),
            categoriesRecipes = persistentListOf(CategoryRecipe.testData(), CategoryRecipe.testData(category = FoodCategory.BEEF)),
            eventSink = {},
        )
    }
}

sealed interface HomeUiEvent : CircuitUiEvent {

    data class OnRecipeClicked(val recipe: Recipe) : HomeUiEvent
    data class OnCategoryClicked(val category: FoodCategory) : HomeUiEvent
    data class OnRecipeLongClick(val title: String) : HomeUiEvent
    data object ToggleDarkTheme : HomeUiEvent
    data object ClearMessage : HomeUiEvent
}

val HomeUiState.showShimmer: Boolean
    get() = categoriesLoading
