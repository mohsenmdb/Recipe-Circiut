package com.me.recipe.ui.recipelist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.shared.utils.FoodCategory
import com.me.recipe.ui.component.util.GenericDialogInfo
import com.me.recipe.ui.component.util.UiMessage
import com.me.recipe.ui.search.SearchState.Companion.testRecipes
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.coroutines.flow.flowOf
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeListScreen(
    val query: String,
) : Screen

typealias RecipeListEventSink = (RecipeListEvent) -> Unit

@Stable
data class RecipeListState(
    val items: LazyPagingItems<Recipe>,
    val errors: GenericDialogInfo? = null,
    val message: UiMessage? = null,
    val query: String = "",
    val selectedCategory: FoodCategory? = null,
    val isEmpty: Boolean = false,
    val eventSink: RecipeListEventSink,
) : CircuitUiState {
    companion object {
        @Composable
        fun testData() = RecipeListState(
            items = flowOf(PagingData.from(testRecipes())).collectAsLazyPagingItems(),
            query = FoodCategory.CHICKEN.name,
            selectedCategory = FoodCategory.CHICKEN,
            eventSink = {},
        )
    }
}

sealed interface RecipeListEvent : CircuitUiEvent {
    data object OnNavigateBackClicked : RecipeListEvent
    data class OnChangeRecipeScrollPosition(val index: Int) : RecipeListEvent
    data class OnRecipeLongClick(val title: String) : RecipeListEvent
    data class OnRecipeClick(val recipe: Recipe) : RecipeListEvent
    data object ClearMessage : RecipeListEvent
}
