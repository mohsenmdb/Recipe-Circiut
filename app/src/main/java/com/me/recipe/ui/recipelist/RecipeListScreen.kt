package com.me.recipe.ui.recipelist

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.me.recipe.R
import com.me.recipe.ui.component.util.NavigateToRecipePage
import com.me.recipe.ui.search.SearchContract
import com.me.recipe.ui.search.SearchViewModel
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.compose.OnClick
import com.me.recipe.util.compose.collectInLaunchedEffect
import com.me.recipe.util.compose.use
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
internal fun RecipeListScreen(
    navigateToRecipePage: NavigateToRecipePage,
    onBackPress: OnClick,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val (state, effect, event) = use(viewModel = viewModel)
    RecipeListScreen(
        effect = effect,
        state = state,
        event = event,
        modifier = modifier,
        navigateToRecipePage = navigateToRecipePage,
        onBackPress = onBackPress,
    )
}

@Composable
@OptIn(InternalCoroutinesApi::class)
private fun RecipeListScreen(
    effect: Flow<SearchContract.Effect>,
    state: SearchContract.State,
    event: (SearchContract.Event) -> Unit,
    navigateToRecipePage: NavigateToRecipePage,
    onBackPress: OnClick,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val actionOk = stringResource(id = R.string.ok)

    effect.collectInLaunchedEffect { effect ->
        when (effect) {
            is SearchContract.Effect.ShowSnackbar -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(effect.message, actionOk)
                }
            }
            is SearchContract.Effect.NavigateToRecipePage -> {
                navigateToRecipePage(effect.recipe)
            }
        }
    }

    // todo fix me
//    Scaffold(
//        snackbarHost = {
//            DefaultSnackbar(snackbarHostState = snackbarHostState) {
//                snackbarHostState.currentSnackbarData?.dismiss()
//            }
//        },
//        topBar = {
//            RecipeListAppBar(
//                category = state.selectedCategory?.value.orEmpty(),
//                onBackPress = onBackPress,
//            )
//        },
//        modifier = modifier,
//    ) { padding ->
//
//        SearchContent(
//            padding = padding,
//            state = state,
//            event = event,
//        )
//    }
}

@Preview
@Composable
private fun RecipeListScreenPreview() {
    RecipeTheme(true) {
        RecipeListScreen(
            event = {},
            effect = flowOf(),
            state = SearchContract.State.testData(),
            navigateToRecipePage = { _ -> },
            onBackPress = { },
        )
    }
}
