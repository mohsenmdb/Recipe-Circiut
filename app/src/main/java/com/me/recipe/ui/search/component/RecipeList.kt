package com.me.recipe.ui.search.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.component.AppendingErrorView
import com.me.recipe.ui.component.AppendingLoadingView
import com.me.recipe.ui.component.util.ErrorFormatterFake
import com.me.recipe.ui.search.SearchState
import com.me.recipe.ui.search.appendErrorOrNull
import com.me.recipe.ui.search.isAppending
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.compose.OnClick
import recipe.app.core.errorformater.ErrorFormatter

@Composable
internal fun RecipeList(
    onRecipeClicked: (Recipe) -> Unit,
    onRecipeLongClicked: (String) -> Unit,
    onAppendingRetryClicked: OnClick,
    errorFormatter: ErrorFormatter,
    modifier: Modifier = Modifier,
    items: LazyPagingItems<Recipe>? = null,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        modifier = modifier
            .padding(top = 8.dp)
            .fillMaxSize()
            .testTag("testTag_recipeList"),
    ) {
        items(
            count = items!!.itemCount,
            key = items.itemKey { it.id },
            itemContent = { index ->
                val recipe = items[index]
                if (recipe != null) {
                    RecipeCard(
                        recipe = recipe,
                        onClick = { onRecipeClicked(recipe) },
                        onLongClick = { onRecipeLongClicked(recipe.title) },
                    )
                }
            },
        )
        if (items.loadState.isAppending()) {
            item {
                AppendingLoadingView()
            }
        }
        items.loadState.appendErrorOrNull()?.let {
            it.throwable?.let(errorFormatter::format)?.let { readableMessage ->
                item {
                    AppendingErrorView(
                        message = readableMessage,
                        onClick = onAppendingRetryClicked,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun RecipeListPreview() {
    RecipeTheme(true) {
        RecipeList(
            onRecipeClicked = {},
            onRecipeLongClicked = {},
            onAppendingRetryClicked = {},
            items = SearchState.testData().items,
            errorFormatter = ErrorFormatterFake(),
        )
    }
}

@Preview
@Composable
private fun RecipeListAppendingPreview() {
    RecipeTheme(true) {
        RecipeList(
            onRecipeClicked = {},
            onRecipeLongClicked = {},
            onAppendingRetryClicked = {},
            errorFormatter = ErrorFormatterFake(),
            items = SearchState.testData(SearchState.appendingTestData()).items,
        )
    }
}

@Preview
@Composable
private fun RecipeListAppendingErrorPreview() {
    RecipeTheme(true) {
        RecipeList(
            onRecipeClicked = {},
            onRecipeLongClicked = {},
            onAppendingRetryClicked = {},
            errorFormatter = ErrorFormatterFake(),
            items = SearchState.testData(SearchState.appendingErrorTestData()).items,
        )
    }
}
