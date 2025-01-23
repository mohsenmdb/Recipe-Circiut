@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.me.recipe.ui.search.component

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.component.util.SharedTransitionLayoutPreview
import com.me.recipe.ui.search.SearchUiState
import com.me.recipe.ui.search.component.shimmer.SearchShimmer
import com.me.recipe.ui.search.showLoadingProgressBar
import com.me.recipe.ui.search.showShimmer
import com.me.recipe.ui.theme.RecipeTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun SearchContent(
    recipes: ImmutableList<Recipe>,
    showShimmer: Boolean,
    showLoadingProgressBar: Boolean,
    onRecipeClicked: (Recipe) -> Unit,
    onRecipeLongClicked: (String) -> Unit,
    onChangeRecipeScrollPosition: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (showShimmer) {
        SearchShimmer(
            imageHeight = 250.dp,
            modifier = modifier,
        )
    } else {
        RecipeList(
            recipes = recipes,
            onRecipeClicked = onRecipeClicked,
            onRecipeLongClicked = onRecipeLongClicked,
            onChangeRecipeScrollPosition = onChangeRecipeScrollPosition,
            showLoadingProgressBar = showLoadingProgressBar,
            modifier = modifier,
        )
    }
}

@Preview
@Composable
private fun SearchContentPreview() {
    RecipeTheme(true) {
        SharedTransitionLayoutPreview {
            SearchContent(
                recipes = SearchUiState.testData().recipes,
                showShimmer = false,
                showLoadingProgressBar = false,
                onRecipeClicked = {},
                onRecipeLongClicked = {},
                onChangeRecipeScrollPosition = {},
            )
        }
    }
}

@Preview
@Composable
private fun SearchContentShimmerPreview() {
    RecipeTheme(true) {
        SharedTransitionLayoutPreview {
            SearchContent(
                recipes = persistentListOf(),
                showShimmer = true,
                showLoadingProgressBar = false,
                onRecipeClicked = {},
                onRecipeLongClicked = {},
                onChangeRecipeScrollPosition = {},
            )
        }
    }
}
