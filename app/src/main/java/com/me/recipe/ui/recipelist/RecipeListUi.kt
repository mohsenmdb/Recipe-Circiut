package com.me.recipe.ui.recipelist

import androidx.activity.compose.BackHandler
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
import com.me.recipe.ui.recipelist.components.RecipeListAppBar
import com.me.recipe.ui.search.component.SearchContent
import com.me.recipe.ui.theme.RecipeTheme
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent

@CircuitInject(RecipeListScreen::class, SingletonComponent::class)
@Composable
internal fun RecipeListUi(
    state: RecipeListState,
    modifier: Modifier = Modifier,
) {
    BackHandler(onBack = { state.eventSink(RecipeListEvent.OnNavigateBackClicked) })
    val snackbarHostState = remember { SnackbarHostState() }
    MessageEffect(
        snackbarHostState = snackbarHostState,
        uiMessage = state.message,
        onClearMessage = { state.eventSink.invoke(RecipeListEvent.ClearMessage) },
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
                onBackPress = { state.eventSink.invoke(RecipeListEvent.OnNavigateBackClicked) },
            )
        },
    ) { padding ->
        // Todo fix me add items
        SearchContent(
            isLoading = state.isLoading,
            isEmpty = state.isEmpty,
            showLoadingProgressBar = state.appendingLoading,
            onRecipeClicked = { state.eventSink.invoke(RecipeListEvent.OnRecipeClick(it)) },
            onRecipeLongClicked = { state.eventSink.invoke(RecipeListEvent.OnRecipeLongClick(it)) },
            onChangeRecipeScrollPosition = { state.eventSink.invoke(RecipeListEvent.OnChangeRecipeScrollPosition(it)) },
            modifier = Modifier.padding(padding),
        )
        state.errors?.let { GenericDialog(it) }
    }
}

@Preview
@Composable
private fun RecipeListScreenPreview() {
    RecipeTheme(true) {
        RecipeListUi(
            state = RecipeListState.testData(),
        )
    }
}
