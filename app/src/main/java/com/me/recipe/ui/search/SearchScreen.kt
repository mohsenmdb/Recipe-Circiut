package com.me.recipe.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.shared.utils.FoodCategory
import com.me.recipe.ui.component.util.GenericDialogInfo
import com.me.recipe.ui.component.util.UiMessage
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import java.net.ConnectException
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchScreen(val query: String = "") : Screen

typealias SearchEventSink = (SearchEvent) -> Unit

@Stable
data class SearchState(
    val items: LazyPagingItems<Recipe>,
    val errors: GenericDialogInfo? = null,
    val message: UiMessage? = null,
    val query: String = "",
    val selectedCategory: FoodCategory? = null,
    val eventSink: SearchEventSink,
) : CircuitUiState {
    companion object {
        fun testRecipes(): ImmutableList<Recipe> = persistentListOf(
            Recipe.testData(),
            Recipe.testData().copy(
                id = 2,
                uid = "uid-2",
                title = "Chicken Curry",
                description = "This is Chicken Curry",
                publisher = "Spice House",
                rating = "18",
                date = "Nov 13 2025",
            ),
        )

        fun appendingTestData() = flowOf(
            PagingData.from(
                data = testRecipes(),
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = false),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.Loading,
                ),
            ),
        )

        fun emptyTestData() = flowOf(
            PagingData.from(
                data = emptyList<Recipe>(),
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = true),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true),
                ),
            ),
        )

        fun errorTestData() = flowOf(
            PagingData.from(
                data = emptyList<Recipe>(),
                sourceLoadStates = LoadStates(
                    refresh = LoadState.Error(ConnectException()),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true),
                ),
            ),
        )

        fun appendingErrorTestData() = flowOf(
            PagingData.from(
                data = testRecipes(),
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = true),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.Error(ConnectException()),
                ),
            ),
        )

        @Composable
        fun testData(
            pagingDataFlow: Flow<PagingData<Recipe>> = flowOf(PagingData.from(testRecipes())),
        ) = SearchState(
            items = pagingDataFlow.collectAsLazyPagingItems(),
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
    data class OnSelectedCategoryChanged(val category: String) : SearchEvent
    data class OnRecipeLongClick(val title: String) : SearchEvent
    data class OnRecipeClick(val recipe: Recipe) : SearchEvent
    data object OngRetryClicked : SearchEvent
    data object OnAppendingRetryClicked : SearchEvent
    data object ClearMessage : SearchEvent
}
