package com.me.recipe.ui.recipe.components.shimmer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.ui.recipe.components.chip.LoadingRankChip
import com.me.recipe.ui.search.component.shimmer.RecipeShimmer

@Composable
internal fun RecipeShimmerView(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize()) {
        RecipeShimmer(
            itemCount = 1,
            withSmallView = false,
            imageHeight = 500.dp,
            modifier = Modifier
                .height(500.dp)
                .testTag("testTag_recipeImage_shimmer"),
        )
        LoadingRankChip(
            Modifier
                .padding(top = 16.dp, end = 16.dp)
                .align(Alignment.End),
        )
        LoadingRecipeShimmer(Modifier.padding(horizontal = 16.dp))
    }
}

@Preview
@Composable
private fun RecipeShimmerPreView() {
    RecipeShimmerView()
}
