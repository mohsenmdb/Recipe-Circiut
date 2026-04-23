package com.me.recipe.ui.profile.myrecipes

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
import com.me.recipe.ui.search.component.SearchContent
import com.me.recipe.ui.theme.RecipeTheme
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent

@CircuitInject(MyRecipesScreen::class, SingletonComponent::class)
@Composable
internal fun MyRecipesUi(
    state: MyRecipesState,
    modifier: Modifier = Modifier,
) {
    BackHandler(onBack = { state.eventSink(MyRecipesEvent.OnNavigateBackClicked) })
    val snackbarHostState = remember { SnackbarHostState() }
    MessageEffect(
        snackbarHostState = snackbarHostState,
        uiMessage = state.message,
        onClearMessage = { state.eventSink.invoke(MyRecipesEvent.ClearMessage) },
    )
    Scaffold(
        modifier = modifier,
        snackbarHost = {
            DefaultSnackbar(snackbarHostState = snackbarHostState) {
                snackbarHostState.currentSnackbarData?.dismiss()
            }
        },
    ) { padding ->
        SearchContent(
            items = state.items,
            onRecipeClicked = { state.eventSink.invoke(MyRecipesEvent.OnRecipeClick(it)) },
            onRecipeLongClicked = { state.eventSink.invoke(MyRecipesEvent.OnRecipeLongClick(it)) },
            onAppendingRetryClicked = { state.eventSink.invoke(MyRecipesEvent.OnAppendingRetryClicked) },
            onRetryClicked = { state.eventSink.invoke(MyRecipesEvent.OnRetryClicked) },
            modifier = Modifier.padding(padding),
        )
        state.errors?.let { GenericDialog(it) }
    }
}

@Preview
@Composable
private fun MyRecipesScreenPreview() {
    RecipeTheme(true) {
        MyRecipesUi(
            state = MyRecipesState.Companion.testData(),
        )
    }
}
