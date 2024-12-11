@file:OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)

package com.me.recipe.ui.home.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.domain.features.recipe.model.CategoryRecipe
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.component.image.CoilImage
import com.me.recipe.ui.component.util.SharedTransitionLayoutPreview
import com.me.recipe.ui.home.HomeContract
import com.me.recipe.ui.theme.RecipeTheme

@Composable
internal fun RecipeCategoryVerticalItem(
    category: CategoryRecipe,
    event: (HomeContract.Event) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .background(MaterialTheme.colorScheme.background),
    ) {
        CategoryTitleRow(
            category = category.category,
            onCategoryClicked = {
                event.invoke(HomeContract.Event.OnCategoryClick(it))
            },
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(category.recipes) {
                CategoryCardVertical(
                    recipe = it,
                    onClick = {
                        event.invoke(HomeContract.Event.OnRecipeClick(it))
                    },
                    onLongClick = {
                        event.invoke(HomeContract.Event.OnRecipeLongClick(it.title))
                    },
                )
            }
        }
    }
}

@Composable
private fun CategoryCardVertical(
    recipe: Recipe,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
    ) {
        Column {
            CoilImage(
                data = recipe.featuredImage,
                contentDescription = "recipe image",
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp),
                contentScale = ContentScale.Crop,
            )
            RecipeTitle(
                recipe
            )
        }
    }
}

@Composable
private fun RecipeTitle(
    recipe: Recipe,
    modifier: Modifier = Modifier,
) {
    Text(
        text = recipe.title,
        maxLines = 1,
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        style = MaterialTheme.typography.labelMedium,
        color = Color.White,
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF292C3C,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun CategoryCardVerticalPreview() {
    RecipeTheme {
        CategoryCardVertical(
            recipe = Recipe.testData(),
            onClick = {},
            onLongClick = {},
        )
    }
}
