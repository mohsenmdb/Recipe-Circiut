package com.me.recipe.ui.search.component

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
import com.me.recipe.ui.search.SearchState
import com.me.recipe.ui.search.isAppending
import com.me.recipe.ui.theme.RecipeTheme

@Composable
internal fun RecipeList(
    onRecipeClicked: (Recipe) -> Unit,
    onRecipeLongClicked: (String) -> Unit,
    onChangeRecipeScrollPosition: (Int) -> Unit,
    modifier: Modifier = Modifier,
    items: LazyPagingItems<Recipe>? = null,
) {
    LazyColumn(
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
                    onChangeRecipeScrollPosition(index)
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
    }
}

@Preview
@Composable
private fun SearchContentPreview() {
    RecipeTheme(true) {
        RecipeList(
            items = SearchState.testData().items,
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
        RecipeList(
            items = SearchState.testData(SearchState.appendingTestData()).items,
            onRecipeClicked = {},
            onRecipeLongClicked = {},
            onChangeRecipeScrollPosition = {},
        )
    }
}
