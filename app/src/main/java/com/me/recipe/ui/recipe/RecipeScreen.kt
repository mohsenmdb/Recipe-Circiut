package com.me.recipe.ui.recipe

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.me.recipe.ui.component.util.DefaultSnackbar
import com.me.recipe.ui.recipe.components.RecipeDetail
import com.me.recipe.ui.theme.RecipeTheme
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent

@CircuitInject(RecipeUiScreen::class, SingletonComponent::class)
@Composable
internal fun RecipeScreen(
    state: RecipeUiState,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

//    effect.collectInLaunchedEffect { effect ->
//        when (effect) {
//            is RecipeContract.Effect.ShowSnackbar -> {
//                coroutineScope.launch {
//                    snackbarHostState.showSnackbar(effect.message, "Ok")
//                }
//            }
//        }
//    }

    Scaffold(
        snackbarHost = {
            DefaultSnackbar(snackbarHostState = snackbarHostState) {
                snackbarHostState.currentSnackbarData?.dismiss()
            }
        },
    ) { padding ->
        RecipeDetail(
            recipe = state.recipe,
            isLoading = state.recipesLoading,
            modifier = Modifier.padding(padding),
        )
    }
}

@Preview
@Composable
private fun RecipeScreenPreview() {
    RecipeTheme(true) {
        RecipeScreen(
            state = RecipeUiState.testData(),
        )
    }
}
