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
data class HomeScreen(val title: String? = null) : Screen

typealias HomeEventSink = (HomeEvent) -> Unit

@Stable
data class HomeState(
    val sliderRecipes: ImmutableList<Recipe>? = null,
    val categoriesRecipes: ImmutableList<CategoryRecipe>? = null,
    val errors: GenericDialogInfo? = null,
    val sliderLoading: Boolean = sliderRecipes == null,
    val categoriesLoading: Boolean = categoriesRecipes.isNullOrEmpty(),
    val isDark: Boolean = false,
    val message: UiMessage? = null,
    val eventSink: HomeEventSink,
) : CircuitUiState {
    companion object {
        fun testData() = HomeState(
            sliderRecipes = persistentListOf(Recipe.testData()),
            categoriesRecipes = persistentListOf(CategoryRecipe.testData(), CategoryRecipe.testData(category = FoodCategory.BEEF)),
            eventSink = {},
        )
    }
}

sealed interface HomeEvent : CircuitUiEvent {

    data class OnRecipeClicked(val recipe: Recipe) : HomeEvent
    data class OnCategoryClicked(val category: FoodCategory) : HomeEvent
    data class OnRecipeLongClick(val title: String) : HomeEvent
    data object ToggleDarkTheme : HomeEvent
    data object ClearMessage : HomeEvent
    data object OnRetryClicked : HomeEvent
}

val HomeState.showShimmer: Boolean
    get() = categoriesLoading
