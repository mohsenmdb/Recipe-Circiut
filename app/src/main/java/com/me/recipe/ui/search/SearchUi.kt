package com.me.recipe.ui.search

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.me.recipe.ui.component.util.DefaultSnackbar
import com.me.recipe.ui.component.util.GenericDialog
import com.me.recipe.ui.component.util.MessageEffect
import com.me.recipe.ui.search.component.SearchAppBar
import com.me.recipe.ui.search.component.SearchContent
import com.me.recipe.ui.theme.RecipeTheme
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent

@CircuitInject(SearchScreen::class, SingletonComponent::class)
@Composable
internal fun SearchUi(
    state: SearchState,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    MessageEffect(
        snackbarHostState = snackbarHostState,
        uiMessage = state.message,
        onClearMessage = { state.eventSink.invoke(SearchEvent.ClearMessage) },
    )
    Scaffold(
        snackbarHost = {
            DefaultSnackbar(snackbarHostState = snackbarHostState) {
                snackbarHostState.currentSnackbarData?.dismiss()
            }
        },
        topBar = {
            SearchAppBar(
                query = state.query,
                selectedCategory = state.selectedCategory,
                onQueryChanged = { state.eventSink.invoke(SearchEvent.OnQueryChanged(it)) },
                newSearch = { state.eventSink.invoke(SearchEvent.NewSearchEvent) },
                onSearchClearClicked = { state.eventSink.invoke(SearchEvent.SearchClearEvent) },
                onSelectedCategoryChanged = { category ->
                    state.eventSink.invoke(SearchEvent.OnSelectedCategoryChanged(category))
                },
            )
        },
    ) { padding ->
        SearchContent(
            items = state.items,
            isLoading = state.isLoading,
            isEmpty = state.isEmpty,
            showLoadingProgressBar = state.appendingLoading,
            onRecipeClicked = { state.eventSink.invoke(SearchEvent.OnRecipeClick(it)) },
            onRecipeLongClicked = { state.eventSink.invoke(SearchEvent.OnRecipeLongClick(it)) },
            onChangeRecipeScrollPosition = { state.eventSink.invoke(SearchEvent.OnChangeRecipeScrollPosition(it)) },
            modifier = Modifier.padding(padding),
        )

        state.errors?.let { GenericDialog(it) }
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    RecipeTheme(true) {
        SearchUi(
            state = SearchState.testData(),
        )
    }
}
