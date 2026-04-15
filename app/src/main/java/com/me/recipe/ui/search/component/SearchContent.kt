package com.me.recipe.ui.search.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.component.EmptyView
import com.me.recipe.ui.component.ErrorView
import com.me.recipe.ui.component.util.ErrorFormatterFake
import com.me.recipe.ui.component.util.LocalErrorFormatter
import com.me.recipe.ui.search.SearchState
import com.me.recipe.ui.search.component.shimmer.RecipeShimmer
import com.me.recipe.ui.search.isEmpty
import com.me.recipe.ui.search.isRefreshing
import com.me.recipe.ui.search.refreshErrorOrNull
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.compose.OnClick
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun SearchContent(
    items: LazyPagingItems<Recipe>,
    onRecipeClicked: (Recipe) -> Unit,
    onRecipeLongClicked: (String) -> Unit,
    onAppendingRetryClicked: OnClick,
    onRetryClicked: OnClick,
    modifier: Modifier = Modifier,
) {
    val errorFormatter = LocalErrorFormatter.current
    when {
        items.loadState.isRefreshing() && items.itemCount == 0 -> RecipeShimmer(imageHeight = 250.dp, modifier = modifier)

        items.loadState.isEmpty(items.itemCount) -> EmptyView(modifier = modifier)

        items.loadState.refreshErrorOrNull() != null -> {
            items.loadState.refreshErrorOrNull()?.throwable?.let(errorFormatter::format)?.let { readableMessage ->
                ErrorView(message = readableMessage, onClick = onRetryClicked, modifier = modifier)
            }
        }

        else -> {
            RecipeList(
                items = items,
                onRecipeClicked = onRecipeClicked,
                onRecipeLongClicked = onRecipeLongClicked,
                onAppendingRetryClicked = onAppendingRetryClicked,
                errorFormatter = errorFormatter,
                modifier = modifier,
            )
        }
    }
}

@Preview
@Composable
private fun SearchContentPreview() {
    CompositionLocalProvider(LocalErrorFormatter provides ErrorFormatterFake()) {
        RecipeTheme(true) {
            SearchContent(
                items = SearchState.testData().items,
                onRecipeClicked = {},
                onRecipeLongClicked = {},
                onAppendingRetryClicked = {},
                onRetryClicked = {},
            )
        }
    }
}

@Preview
@Composable
private fun SearchContentShimmerPreview() {
    CompositionLocalProvider(LocalErrorFormatter provides ErrorFormatterFake()) {
        RecipeTheme(true) {
            SearchContent(
                items = SearchState.testData(pagingDataFlow = flowOf()).items,
                onRecipeClicked = {},
                onRecipeLongClicked = {},
                onAppendingRetryClicked = {},
                onRetryClicked = {},
            )
        }
    }
}

@Preview
@Composable
private fun SearchContentAppendingPreview() {
    CompositionLocalProvider(LocalErrorFormatter provides ErrorFormatterFake()) {
        RecipeTheme(true) {
            SearchContent(
                items = SearchState.testData(
                    pagingDataFlow = SearchState.appendingTestData(),
                ).items,
                onRecipeClicked = {},
                onRecipeLongClicked = {},
                onAppendingRetryClicked = {},
                onRetryClicked = {},
            )
        }
    }
}

@Preview
@Composable
private fun SearchContentEmptyPreview() {
    CompositionLocalProvider(LocalErrorFormatter provides ErrorFormatterFake()) {
        RecipeTheme(true) {
            SearchContent(
                items = SearchState.testData(SearchState.emptyTestData()).items,
                onRecipeClicked = {},
                onRecipeLongClicked = {},
                onAppendingRetryClicked = {},
                onRetryClicked = {},
            )
        }
    }
}

@Preview
@Composable
private fun SearchContentErrorPreview() {
    CompositionLocalProvider(LocalErrorFormatter provides ErrorFormatterFake()) {
        RecipeTheme(true) {
            SearchContent(
                items = SearchState.testData(SearchState.errorTestData()).items,
                onRecipeClicked = {},
                onRecipeLongClicked = {},
                onAppendingRetryClicked = {},
                onRetryClicked = {},
            )
        }
    }
}
