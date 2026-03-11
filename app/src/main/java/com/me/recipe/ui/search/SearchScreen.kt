package com.me.recipe.ui.search

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
data class SearchScreen(val query: String = "") : Screen

typealias SearchEventSink = (SearchEvent) -> Unit

@Stable
data class SearchState(
    val recipes: ImmutableList<Recipe>,
    val errors: GenericDialogInfo? = null,
    val message: UiMessage? = null,
    val query: String = "",
    val selectedCategory: FoodCategory? = null,
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false,
    val appendingLoading: Boolean = false,
    var categoryScrollPosition: Pair<Int, Int> = 0 to 0,
    val eventSink: SearchEventSink,
) : CircuitUiState {
    companion object {
        fun testData() = SearchState(
            recipes = persistentListOf(Recipe.testData()),
            query = FoodCategory.CHICKEN.name,
            selectedCategory = FoodCategory.CHICKEN,
            eventSink = {},
        )
    }
}

sealed interface SearchEvent : CircuitUiEvent {

    data object NewSearchEvent : SearchEvent
    data object SearchClearEvent : SearchEvent
    data class OnQueryChanged(val query: String) : SearchEvent
    data class OnSelectedCategoryChanged(val category: String, val position: Int = 0, val offset: Int = 0) : SearchEvent
    data class OnRecipeLongClick(val title: String) : SearchEvent
    data class OnRecipeClick(val recipe: Recipe) : SearchEvent
    data class OnChangeRecipeScrollPosition(val index: Int) : SearchEvent
    data object ClearMessage : SearchEvent
}
