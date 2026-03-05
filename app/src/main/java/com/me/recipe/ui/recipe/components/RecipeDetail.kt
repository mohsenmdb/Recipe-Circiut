package com.me.recipe.ui.recipe.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.recipe.components.image.RecipeImage
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.compose.OnClick

@Composable
internal fun RecipeDetail(
    recipe: Recipe,
    onLikeClicked: OnClick,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        if (recipe.image.isNotEmpty()) {
            RecipeImage(image = recipe.image)
        }
        RecipeContent(
            recipe = recipe,
            onLikeClicked = onLikeClicked,
        )
    }
}

@Preview
@Composable
private fun RecipeDetailPreview() {
    RecipeTheme(true) {
        RecipeDetail(
            recipe = Recipe.testData(),
            onLikeClicked = {},
        )
    }
}
