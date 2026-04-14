package com.me.recipe.ui.search.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.component.EmptyView
import com.me.recipe.ui.search.SearchState
import com.me.recipe.ui.search.component.shimmer.RecipeShimmer
import com.me.recipe.ui.search.isRefreshing
import com.me.recipe.ui.theme.RecipeTheme
import kotlinx.coroutines.flow.flowOf

@Composable
internal fun SearchContent(
    items: LazyPagingItems<Recipe>,
    isEmpty: Boolean,
    onRecipeClicked: (Recipe) -> Unit,
    onRecipeLongClicked: (String) -> Unit,
    onChangeRecipeScrollPosition: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        items.loadState.isRefreshing() && items.itemCount == 0 -> RecipeShimmer(imageHeight = 250.dp)

        isEmpty -> EmptyView()

        else -> {
            RecipeList(
                items = items,
                onRecipeClicked = onRecipeClicked,
                onRecipeLongClicked = onRecipeLongClicked,
                onChangeRecipeScrollPosition = onChangeRecipeScrollPosition,
                modifier = modifier,
            )
        }
    }
}

@Preview
@Composable
private fun SearchContentPreview() {
    RecipeTheme(true) {
        SearchContent(
            items = SearchState.testData().items,
            isEmpty = false,
            onRecipeClicked = {},
            onRecipeLongClicked = {},
            onChangeRecipeScrollPosition = {},
        )
    }
}

@Preview
@Composable
private fun SearchContentShimmerPreview() {
    RecipeTheme(true) {
        SearchContent(
            items = SearchState.testData(pagingDataFlow = flowOf()).items,
            isEmpty = true,
            onRecipeClicked = {},
            onRecipeLongClicked = {},
            onChangeRecipeScrollPosition = {},
        )
    }
}

@Preview
@Composable
private fun SearchContentAppendingPreview() {
    RecipeTheme(true) {
        SearchContent(
            items = SearchState.testData(
                pagingDataFlow = SearchState.appendingTestData(),
            ).items,
            isEmpty = false,
            onRecipeClicked = {},
            onRecipeLongClicked = {},
            onChangeRecipeScrollPosition = {},
        )
    }
}

@Preview
@Composable
private fun SearchContentEmptyPreview() {
    RecipeTheme(true) {
        SearchContent(
            items = SearchState.testData().items,
            isEmpty = true,
            onRecipeClicked = {},
            onRecipeLongClicked = {},
            onChangeRecipeScrollPosition = {},
        )
    }
}
