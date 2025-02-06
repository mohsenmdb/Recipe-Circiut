package com.me.recipe.ui.recipe.components.image

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.component.image.CoilImage
import com.me.recipe.ui.theme.RecipeTheme

@Composable
internal fun RecipeImage(
    image: String,
    modifier: Modifier = Modifier,
) {
    CoilImage(
        data = image,
        contentDescription = "Recipe Featured Image",
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .testTag("testTag_RecipeImage"),
        contentScale = ContentScale.Crop,
    )
}

@Preview
@Composable
private fun RecipeImagePreview() {
    RecipeTheme(true) {
        RecipeImage(
            image = Recipe.testData().featuredImage,
        )
    }
}
