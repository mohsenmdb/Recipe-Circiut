@file:OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)

package com.me.recipe.ui.home.components

import android.content.res.Configuration
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.domain.features.recipe.model.CategoryRecipe
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.component.image.CoilImage
import com.me.recipe.ui.theme.RecipeTheme

@Composable
internal fun RecipeCategoryHorizontal(
    category: CategoryRecipe,
    onRecipeClick: OnRecipeClick = {},
    onCategoryClick: OnCategoryClick = {},
    onRecipeLongClick: OnRecipeLongClick = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
    ) {
        CategoryTitleRow(
            category = category.category,
            onCategoryClicked = {
                onCategoryClick(it)
            },
        )
        Spacer(Modifier.height(20.dp))
        CategoryHorizontalLazyRow(
            category = category,
            onRecipeClick = onRecipeClick,
            onRecipeLongClick = onRecipeLongClick,
        )
    }
}

@Composable
private fun CategoryHorizontalLazyRow(
    category: CategoryRecipe,
    onRecipeClick: OnRecipeClick,
    onRecipeLongClick: OnRecipeLongClick,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.testTag("testTag_category_horizontal_lazyRow"),
    ) {
        items(category.recipes) {
            CategoryCardHorizontal(
                recipe = it,
                onClick = {
                    onRecipeClick(it)
                },
                onLongClick = {
                    onRecipeLongClick(it.title)
                },
            )
        }
    }
}

@Composable
internal fun CategoryCardHorizontal(
    recipe: Recipe,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
            .width(250.dp)
            .height(150.dp)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
    ) {
        CoilImage(
            data = recipe.featuredImage,
            contentDescription = "recipe image",
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF292C3C,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun CategoryCardHorizontalPreview() {
    RecipeTheme {
        CategoryCardHorizontal(
            recipe = Recipe.testData(),
            onClick = {},
            onLongClick = {},
        )
    }
}
