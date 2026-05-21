package com.me.recipe.ui.recipe

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.component.util.DefaultSnackbar
import com.me.recipe.ui.component.util.MessageEffect
import com.me.recipe.ui.component.util.UiMessage
import com.me.recipe.ui.recipe.components.RecipeDetail
import com.me.recipe.ui.recipe.components.shimmer.RecipeShimmerView
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.compose.OnClick
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent

@CircuitInject(RecipeScreen::class, SingletonComponent::class)
@Composable
internal fun RecipeUi(
    state: RecipeState,
    modifier: Modifier = Modifier,
) {
    BackHandler(onBack = { state.eventSink(RecipeEvent.OnBackClicked) })

    RecipeContent(
        isLoading = state.isLoading,
        message = state.message,
        recipe = state.recipe,
        clearMessage = { state.eventSink(RecipeEvent.ClearMessage) },
        onLikeClicked = { state.eventSink(RecipeEvent.OnLikeClicked) },
        modifier = modifier,
    )
}

@Composable
internal fun RecipeContent(
    recipe: Recipe,
    isLoading: Boolean,
    message: UiMessage?,
    clearMessage: OnClick,
    onLikeClicked: OnClick,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    MessageEffect(
        snackbarHostState = snackbarHostState,
        uiMessage = message,
        onClearMessage = clearMessage,
    )
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = {
            DefaultSnackbar(snackbarHostState = snackbarHostState) {
                snackbarHostState.currentSnackbarData?.dismiss()
            }
        },
        modifier = modifier,
    ) { padding ->
        if (isLoading) {
            RecipeShimmerView()
        } else {
            RecipeDetail(
                recipe = recipe,
                modifier = Modifier.padding(padding),
                onLikeClicked = onLikeClicked,
            )
        }
    }
}

@Preview
@Composable
private fun RecipeUiPreview() {
    RecipeTheme(true) {
        RecipeUi(
            state = RecipeState.testData(),
        )
    }
}
