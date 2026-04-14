package com.me.recipe.ui.search.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.me.recipe.domain.features.recipe.model.Recipe

@Composable
internal fun RecipeList(
    onRecipeClicked: (Recipe) -> Unit,
    onRecipeLongClicked: (String) -> Unit,
    onChangeRecipeScrollPosition: (Int) -> Unit,
    modifier: Modifier = Modifier,
    showLoadingProgressBar: Boolean = false,
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
        if (showLoadingProgressBar) {
            item {
                AppendingLoadingView()
            }
        }
    }
}

// TODO fix me
// @Preview
// @Composable
// private fun SearchContentPreview() {
//    RecipeTheme(true) {
//        RecipeList(
//            recipes = SearchContract.State.testData().recipes,
//            onRecipeClicked = {},
//            onRecipeLongClicked = {},
//            onChangeRecipeScrollPosition = {},
//        )
//    }
// }
