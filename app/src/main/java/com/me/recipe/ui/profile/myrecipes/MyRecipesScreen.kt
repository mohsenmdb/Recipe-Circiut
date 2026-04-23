package com.me.recipe.ui.profile.myrecipes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.component.util.GenericDialogInfo
import com.me.recipe.ui.component.util.UiMessage
import com.me.recipe.ui.search.SearchState.Companion.testRecipes
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.coroutines.flow.flowOf
import kotlinx.parcelize.Parcelize

@Parcelize
data object MyRecipesScreen : Screen

typealias MyRecipesEventSink = (MyRecipesEvent) -> Unit

@Stable
data class MyRecipesState(
    val items: LazyPagingItems<Recipe>,
    val errors: GenericDialogInfo? = null,
    val message: UiMessage? = null,
    val eventSink: MyRecipesEventSink,
) : CircuitUiState {
    companion object {
        @Composable
        fun testData() = MyRecipesState(
            items = flowOf(PagingData.from(testRecipes())).collectAsLazyPagingItems(),
            eventSink = {},
        )
    }
}

sealed interface MyRecipesEvent : CircuitUiEvent {
    data object OnNavigateBackClicked : MyRecipesEvent
    data object OnRetryClicked : MyRecipesEvent
    data object OnAppendingRetryClicked : MyRecipesEvent
    data class OnRecipeLongClick(val title: String) : MyRecipesEvent
    data class OnRecipeClick(val recipe: Recipe) : MyRecipesEvent
    data object ClearMessage : MyRecipesEvent
}
