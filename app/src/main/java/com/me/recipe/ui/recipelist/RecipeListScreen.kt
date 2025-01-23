package com.me.recipe.ui.recipelist

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.me.recipe.ui.component.util.DefaultSnackbar
import com.me.recipe.ui.component.util.GenericDialog
import com.me.recipe.ui.component.util.SnackbarEffect
import com.me.recipe.ui.recipelist.components.RecipeListAppBar
import com.me.recipe.ui.search.SearchScreen
import com.me.recipe.ui.search.component.SearchContent
import com.me.recipe.ui.theme.RecipeTheme
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent


@CircuitInject(RecipeListScreen::class, SingletonComponent::class)
@Composable
internal fun RecipeListScreen(
    state: RecipeListUiState,
    modifier: Modifier = Modifier
) {

    val snackbarHostState = remember { SnackbarHostState() }
    SnackbarEffect(
        snackbarHostState = snackbarHostState,
        message = state.message,
        onClearMessage = { state.eventSink.invoke(RecipeListUiEvent.ClearMessage) }
    )
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            DefaultSnackbar(snackbarHostState = snackbarHostState) {
                snackbarHostState.currentSnackbarData?.dismiss()
            }
        },
        topBar = {
            RecipeListAppBar(
                category = state.query,
                onBackPress = { state.eventSink.invoke(RecipeListUiEvent.OnNavigateBackClicked) },
            )
        },
    ) { padding ->
        SearchContent(
            recipes = state.recipes,
            showShimmer = state.loading,
            showLoadingProgressBar = state.appendingLoading,
            onRecipeClicked = { state.eventSink.invoke(RecipeListUiEvent.OnRecipeClick(it)) },
            onRecipeLongClicked = { state.eventSink.invoke(RecipeListUiEvent.OnRecipeLongClick(it)) },
            onChangeRecipeScrollPosition = { state.eventSink.invoke(RecipeListUiEvent.OnChangeRecipeScrollPosition(it)) },
            modifier = Modifier.padding(padding)
        )
        state.errors?.let { GenericDialog(it) }
    }
}

@Preview
@Composable
private fun RecipeListScreenPreview() {
    RecipeTheme(true) {
        RecipeListScreen(
            state = RecipeListUiState.testData()
        )
    }
}
