@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.me.recipe.ui.home

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.ui.component.util.DefaultSnackbar
import com.me.recipe.ui.component.util.MessageEffect
import com.me.recipe.ui.home.components.HomeAppBar
import com.me.recipe.ui.home.components.HomeContent
import com.me.recipe.ui.home.components.shimmer.HomeShimmer
import com.me.recipe.ui.theme.RecipeTheme
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent

@CircuitInject(MainUiScreen::class, SingletonComponent::class)
@Composable
fun HomeScreen(
    state: MainUiState,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    MessageEffect(
        snackbarHostState = snackbarHostState,
        message = state.message,
        onClearMessage = { state.eventSink.invoke(MainUiEvent.ClearMessage) },
    )

    Scaffold(
        snackbarHost = {
            DefaultSnackbar(snackbarHostState = snackbarHostState) {
                snackbarHostState.currentSnackbarData?.dismiss()
            }
        },
        topBar = {
            HomeAppBar(
                isDark = state.isDark,
                onToggleTheme = { state.eventSink(MainUiEvent.ToggleDarkTheme) },
            )
        },
        modifier = modifier.padding(bottom = 80.dp),
    ) { padding ->
        if (state.showShimmer) {
            HomeShimmer(modifier = Modifier.padding(padding))
            return@Scaffold
        }
        HomeContent(
            padding = padding,
            state = state,
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    RecipeTheme(true) {
        HomeScreen(
            state = MainUiState.testData(),
        )
    }
}
