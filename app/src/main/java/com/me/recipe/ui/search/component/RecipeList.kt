package com.me.recipe.ui.search.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.me.recipe.domain.features.recipe.model.Recipe
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun RecipeList(
    recipes: ImmutableList<Recipe>,
    onRecipeClicked: (Recipe) -> Unit,
    onRecipeLongClicked: (String) -> Unit,
    onChangeRecipeScrollPosition: (Int) -> Unit,
    modifier: Modifier = Modifier,
    showLoadingProgressBar: Boolean = false,
) {
    LazyColumn(
        modifier = modifier
            .padding(top = 8.dp)
            .fillMaxSize()
            .testTag("testTag_recipeList"),
    ) {
        itemsIndexed(recipes) { index, recipe ->
            onChangeRecipeScrollPosition(index)
            RecipeCard(
                recipe = recipe,
                onClick = { onRecipeClicked(recipe) },
                onLongClick = { onRecipeLongClicked(recipe.title) },
            )
        }
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
