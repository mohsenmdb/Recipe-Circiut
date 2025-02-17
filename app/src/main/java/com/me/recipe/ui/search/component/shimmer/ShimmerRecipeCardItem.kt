package com.me.recipe.ui.search.component.shimmer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.me.recipe.ui.theme.RecipeTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
internal fun ShimmerRecipeCardItem(
    colors: ImmutableList<Color>,
    xShimmer: Float,
    yShimmer: Float,
    cardHeight: Dp,
    gradientWidth: Float,
    padding: Dp,
    modifier: Modifier = Modifier,
    cardNumber: Int = 0,
    withSmallView: Boolean = true,
) {
    val brush = linearGradient(
        colors,
        start = Offset(xShimmer - gradientWidth, yShimmer - gradientWidth),
        end = Offset(xShimmer, yShimmer),
    )
    Column(
        modifier = modifier
            .padding(padding)
            .testTag("testTag_ShimmerRecipeCardItem_$cardNumber"),
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .size(cardHeight)
                    .background(brush = brush),
            )
        }
        if (withSmallView) {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .padding(vertical = 8.dp),
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(cardHeight / 10)
                        .background(brush = brush),
                )
            }
        }
    }
}

@Preview
@Composable
private fun ShimmerRecipeCardItemPreview() {
    RecipeTheme(true) {
        ShimmerRecipeCardItem(
            colors = persistentListOf(
                Color.LightGray.copy(alpha = .9f),
                Color.LightGray.copy(alpha = .3f),
                Color.LightGray.copy(alpha = .9f),
            ),
            xShimmer = 250f,
            yShimmer = 250f,
            cardHeight = 250.dp,
            gradientWidth = 30f,
            padding = 5.dp,
            cardNumber = 0,
        )
    }
}

@Preview
@Composable
private fun ShimmerRecipeCardItemWithOutSmallViewPreview() {
    RecipeTheme(true) {
        ShimmerRecipeCardItem(
            colors = persistentListOf(
                Color.LightGray.copy(alpha = .9f),
                Color.LightGray.copy(alpha = .3f),
                Color.LightGray.copy(alpha = .9f),
            ),
            xShimmer = 250f,
            yShimmer = 250f,
            cardHeight = 250.dp,
            gradientWidth = 30f,
            padding = 5.dp,
            cardNumber = 0,
            withSmallView = false,
        )
    }
}
