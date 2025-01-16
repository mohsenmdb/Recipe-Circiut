@file:OptIn(ExperimentalSharedTransitionApi::class, InternalCoroutinesApi::class)

package com.me.recipe.ui.search

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.me.recipe.R
import com.me.recipe.ui.component.util.DefaultSnackbar
import com.me.recipe.ui.component.util.GenericDialog
import com.me.recipe.ui.component.util.NavigateToHomePage
import com.me.recipe.ui.component.util.NavigateToRecipePage
import com.me.recipe.ui.component.util.SharedTransitionLayoutPreview
import com.me.recipe.ui.home.MainUiScreen
import com.me.recipe.ui.home.MainUiState
import com.me.recipe.ui.search.component.SearchAppBar
import com.me.recipe.ui.search.component.SearchContent
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.compose.collectInLaunchedEffect
import com.me.recipe.util.compose.use
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@CircuitInject(SearchScreen::class, SingletonComponent::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RememberReturnType")
@Composable
internal fun SearchScreen(
    state: SearchUiState,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val actionOk = stringResource(id = R.string.ok)

    BackHandler {
//        navigateToHomePage.invoke()
    }

//    effect.collectInLaunchedEffect { effect ->
//        when (effect) {
//            is SearchContract.Effect.ShowSnackbar -> {
//                coroutineScope.launch {
//                    snackbarHostState.showSnackbar(effect.message, actionOk)
//                }
//            }
//            is SearchContract.Effect.NavigateToRecipePage -> {
//                navigateToRecipePage(effect.recipe)
//            }
//        }
//    }

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
        modifier = modifier,
    ) { padding ->
        SearchContent(
            recipes = state.recipes,
            showShimmer = state.showShimmer,
            showLoadingProgressBar = state.showLoadingProgressBar,
            onRecipeClicked = { state.eventSink.invoke(SearchUiEvent.OnRecipeClick(it)) },
            onRecipeLongClicked = { state.eventSink.invoke(SearchUiEvent.OnRecipeLongClick(it)) },
            onChangeRecipeScrollPosition = { state.eventSink.invoke(SearchUiEvent.OnChangeRecipeScrollPosition(it)) },
            modifier = Modifier.padding(padding)
        )

        state.errors?.let { GenericDialog(it) }
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    RecipeTheme(true) {
        SearchScreen(
            state = SearchUiState.testData(),
        )
    }
}
