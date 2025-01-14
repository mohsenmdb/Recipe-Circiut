@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.me.recipe.ui.home.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.shared.utils.FoodCategory
import com.me.recipe.ui.component.util.SharedTransitionLayoutPreview
import com.me.recipe.ui.home.HomeContract
import com.me.recipe.ui.home.MainUiEvent
import com.me.recipe.ui.home.MainUiState
import com.me.recipe.ui.theme.RecipeTheme

typealias OnRecipeClick = (Recipe) -> Unit
typealias OnCategoryClick = (FoodCategory) -> Unit
typealias OnRecipeLongClick = (String) -> Unit

@Composable
internal fun HomeContent(
    padding: PaddingValues,
    state: MainUiState,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(MaterialTheme.colorScheme.background),
    ) {
        if (!state.sliderRecipes.isNullOrEmpty()){
            item {
                HomeSlider(
                    recipes = state.sliderRecipes,
                    onRecipeClick = { state.eventSink.invoke(MainUiEvent.OnRecipeClicked(it)) },
                )
            }
        }

        if (!state.categoriesRecipes.isNullOrEmpty()) {
            itemsIndexed(state.categoriesRecipes) { index, category ->
                if (index == 0) {
                    RecipeCategoryHorizontalItem(
                        category = category,
                        onRecipeClick = { state.eventSink.invoke(MainUiEvent.OnRecipeClicked(it)) },
                        onCategoryClick = { state.eventSink.invoke(MainUiEvent.OnCategoryClicked(it)) },
                        onRecipeLongClick = { state.eventSink.invoke(MainUiEvent.OnRecipeLongClick(it)) },
                    )
                    return@itemsIndexed
                }
                RecipeCategoryVerticalItem(
                    category = category,
                    onRecipeClick = { state.eventSink.invoke(MainUiEvent.OnRecipeClicked(it)) },
                    onCategoryClick = { state.eventSink.invoke(MainUiEvent.OnCategoryClicked(it)) },
                    onRecipeLongClick = { state.eventSink.invoke(MainUiEvent.OnRecipeLongClick(it)) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeContentPreview() {
    RecipeTheme(true) {
        HomeContent(
            padding = PaddingValues(16.dp),
            state = MainUiState.testData(),
        )
    }
}
