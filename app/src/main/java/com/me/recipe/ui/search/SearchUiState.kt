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

typealias SearchEventSink = (SearchUiEvent) -> Unit

@Stable
data class SearchUiState(
    val recipes: ImmutableList<Recipe>,
    val errors: GenericDialogInfo? = null,
    val message: UiMessage? = null,
    val query: String = "",
    val selectedCategory: FoodCategory? = null,
    val loading: Boolean = false,
    val appendingLoading: Boolean = false,
    var categoryScrollPosition: Pair<Int, Int> = 0 to 0,
    val eventSink: SearchEventSink,
) : CircuitUiState {
    companion object {
        fun testData() = SearchUiState(
            recipes = persistentListOf(Recipe.testData()),
            query = FoodCategory.CHICKEN.name,
            selectedCategory = FoodCategory.CHICKEN,
            eventSink = {},
        )
    }
}

sealed interface SearchUiEvent : CircuitUiEvent {

    data object NewSearchEvent : SearchUiEvent
    data object SearchClearEvent : SearchUiEvent
    data class OnQueryChanged(val query: String) : SearchUiEvent
    data class OnSelectedCategoryChanged(val category: String, val position: Int = 0, val offset: Int = 0) : SearchUiEvent
    data class OnRecipeLongClick(val title: String) : SearchUiEvent
    data class OnRecipeClick(val recipe: Recipe) : SearchUiEvent
    data class OnChangeRecipeScrollPosition(val index: Int) : SearchUiEvent
    data object ClearMessage : SearchUiEvent
}
