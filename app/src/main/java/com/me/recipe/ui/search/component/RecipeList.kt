@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.me.recipe.ui.search.component

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.component.util.SharedTransitionLayoutPreview
import com.me.recipe.ui.search.SearchContract
import com.me.recipe.ui.theme.RecipeTheme
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun RecipeList(
    recipes: ImmutableList<Recipe>,
    onRecipeClicked: (Recipe) -> Unit,
    onRecipeLongClicked: (String) -> Unit,
    onChangeRecipeScrollPosition: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .padding(top = 8.dp)
            .testTag("testTag_recipeList"),
    ) {
        itemsIndexed(recipes) { index, recipe ->
            //Todo  fix me
            onChangeRecipeScrollPosition(index)

            RecipeCard(
                recipe = recipe,
                onClick = {
                    onRecipeClicked(recipe)
                },
                onLongClick = {
                    onRecipeLongClicked(recipe.title)
                },
            )
        }
    }
}

@Preview
@Composable
private fun SearchContentPreview() {
    RecipeTheme(true) {
        RecipeList(
            recipes = SearchContract.State.testData().recipes,
            onRecipeClicked = {},
            onRecipeLongClicked = {},
            onChangeRecipeScrollPosition = {},
        )
    }
}
