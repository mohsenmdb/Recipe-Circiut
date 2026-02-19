package com.me.recipe.ui.home.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.shared.utils.FoodCategory
import com.me.recipe.ui.home.HomeEvent
import com.me.recipe.ui.home.HomeState
import com.me.recipe.ui.theme.RecipeTheme

typealias OnRecipeClick = (Recipe) -> Unit
typealias OnCategoryClick = (FoodCategory) -> Unit
typealias OnRecipeLongClick = (String) -> Unit

@Composable
internal fun HomeContent(
    padding: PaddingValues,
    state: HomeState,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
    ) {
        if (!state.sliderRecipes.isNullOrEmpty()) {
            item {
                HomeSlider(
                    recipes = state.sliderRecipes,
                    onRecipeClick = { state.eventSink.invoke(HomeEvent.OnRecipeClicked(it)) },
                )
            }
        }

        if (!state.categoriesRecipes.isNullOrEmpty()) {
            itemsIndexed(state.categoriesRecipes) { index, category ->
                if (index == 0) {
                    RecipeCategoryHorizontal(
                        category = category,
                        onRecipeClick = { state.eventSink.invoke(HomeEvent.OnRecipeClicked(it)) },
                        onCategoryClick = { state.eventSink.invoke(HomeEvent.OnCategoryClicked(it)) },
                        onRecipeLongClick = { state.eventSink.invoke(HomeEvent.OnRecipeLongClick(it)) },
                    )
                } else {
                    RecipeCategoryVertical(
                        category = category,
                        onRecipeClick = { state.eventSink.invoke(HomeEvent.OnRecipeClicked(it)) },
                        onCategoryClick = { state.eventSink.invoke(HomeEvent.OnCategoryClicked(it)) },
                        onRecipeLongClick = { state.eventSink.invoke(HomeEvent.OnRecipeLongClick(it)) },
                    )
                }
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
            state = HomeState.testData(),
        )
    }
}
