package com.me.recipe.ui.search

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
internal fun SearchScreenView(
    state: SearchUiState,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    MessageEffect(
        snackbarHostState = snackbarHostState,
        message = state.message,
        onClearMessage = { state.eventSink.invoke(SearchUiEvent.ClearMessage) },
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
                categoryScrollPosition = state.categoryScrollPosition,
                onQueryChanged = { state.eventSink.invoke(SearchUiEvent.OnQueryChanged(it)) },
                newSearch = { state.eventSink.invoke(SearchUiEvent.NewSearchEvent) },
                onSearchClearClicked = { state.eventSink.invoke(SearchUiEvent.SearchClearEvent) },
                onSelectedCategoryChanged = { category, position, offset ->
                    state.eventSink.invoke(
                        SearchUiEvent.OnSelectedCategoryChanged(category, position, offset),
                    )
                },
            )
        },
        modifier = modifier.padding(bottom = 80.dp),
    ) { padding ->
        SearchContent(
            recipes = state.recipes,
            showShimmer = state.loading,
            showLoadingProgressBar = state.appendingLoading,
            onRecipeClicked = { state.eventSink.invoke(SearchUiEvent.OnRecipeClick(it)) },
            onRecipeLongClicked = { state.eventSink.invoke(SearchUiEvent.OnRecipeLongClick(it)) },
            onChangeRecipeScrollPosition = { state.eventSink.invoke(SearchUiEvent.OnChangeRecipeScrollPosition(it)) },
            modifier = Modifier.padding(padding),
        )

        state.errors?.let { GenericDialog(it) }
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    RecipeTheme(true) {
        SearchScreenView(
            state = SearchUiState.testData(),
        )
    }
}
