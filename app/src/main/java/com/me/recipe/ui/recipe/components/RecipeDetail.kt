package com.me.recipe.ui.recipe.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.recipe.components.image.RecipeImage
import com.me.recipe.ui.search.component.shimmer.RecipeShimmer
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.compose.OnClick

@Composable
internal fun RecipeDetail(
    recipe: Recipe?,
    isLoading: Boolean,
    onLikeClicked: OnClick,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        if (!recipe?.image.isNullOrEmpty()) {
            RecipeImage(image = recipe?.image!!)
        } else {
            RecipeShimmer(
                itemCount = 1,
                withSmallView = false,
                modifier = Modifier
                    .height(250.dp)
                    .testTag("testTag_recipeImage_shimmer"),
            )
        }
        RecipeContent(
            recipe = recipe,
            isLoading = isLoading,
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
            isLoading = false,
            onLikeClicked = {},
        )
    }
}
