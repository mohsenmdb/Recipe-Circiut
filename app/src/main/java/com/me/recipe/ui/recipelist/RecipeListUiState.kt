package com.me.recipe.ui.recipelist

import androidx.compose.runtime.Stable
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
data class RecipeListScreen(
    val query: String,
) : Screen


typealias RecipeListEventSink = (RecipeListUiEvent) -> Unit

@Stable
data class RecipeListUiState(
    val recipes: ImmutableList<Recipe>,
    val errors: GenericDialogInfo? = null,
    val message: UiMessage? = null,
    val query: String = "",
    val selectedCategory: FoodCategory? = null,
    val loading: Boolean = false,
    val appendingLoading: Boolean = false,
    var categoryScrollPosition: Pair<Int, Int> = 0 to 0,
    val eventSink: RecipeListEventSink,
) : CircuitUiState {
    companion object {
        fun testData() = RecipeListUiState(
            recipes = persistentListOf(Recipe.testData()),
            query = FoodCategory.CHICKEN.name,
            selectedCategory = FoodCategory.CHICKEN,
            eventSink = {}
        )
    }
}

sealed interface RecipeListUiEvent : CircuitUiEvent {
    data object OnNavigateBackClicked : RecipeListUiEvent
    data class OnChangeRecipeScrollPosition(val index: Int) : RecipeListUiEvent
    data class OnRecipeLongClick(val title: String) : RecipeListUiEvent
    data class OnRecipeClick(val recipe: Recipe) : RecipeListUiEvent
    data object ClearMessage : RecipeListUiEvent
}
