@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.me.recipe.ui.home

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.me.recipe.R
import com.me.recipe.ui.component.util.DefaultSnackbar
import com.me.recipe.ui.home.components.HomeAppBar
import com.me.recipe.ui.home.components.HomeContent
import com.me.recipe.ui.home.components.shimmer.HomeShimmer
import com.me.recipe.ui.theme.RecipeTheme
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import timber.log.Timber

@Composable
internal fun HomeScreenOld(
    state: MainUiState,
    modifier: Modifier = Modifier,
) {
//    val viewModel: HomeViewModel = hiltViewModel()
//    val (state, effect, event) = use(viewModel = viewModel)
    Timber.d("MainViewPresenter categoriesRecipes state Recipes= ${state.categoriesRecipes?.size} slider= ${state.sliderRecipes?.size}")
    HomeScreen(
        state = state,
        modifier = modifier,
    )
}
// @Composable
// internal fun HomeScreen(
//    navigateToRecipePage: NavigateToRecipePage,
//    navigateToRecipeListPage: NavigateToRecipeListPage,
//    modifier: Modifier = Modifier,
//    viewModel: HomeViewModel = hiltViewModel(),
// ) {
//    val (state, effect, event) = use(viewModel = viewModel)
//    HomeScreen(
//        effect = effect,
//        state = state,
//        event = event,
//        modifier = modifier,
//        navigateToRecipePage = navigateToRecipePage,
//        navigateToRecipeListPage = navigateToRecipeListPage,
//    )
// }

@CircuitInject(MainUiScreen::class, SingletonComponent::class)
@Composable
fun HomeScreen(
    state: MainUiState,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val actionOk = stringResource(id = R.string.ok)

//    effect.collectInLaunchedEffect { effect ->
//        when (effect) {
//            is HomeContract.Effect.ShowSnackbar -> {
//                coroutineScope.launch {
//                    snackbarHostState.showSnackbar(effect.message, actionOk)
//                }
//            }
//            is HomeContract.Effect.NavigateToRecipePage -> navigateToRecipePage(effect.recipe)
//            is HomeContract.Effect.NavigateToRecipeListPage -> navigateToRecipeListPage(effect.category)
//        }
//    }

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
        modifier = modifier,
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
