package com.me.recipe.ui.recipe.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.R
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.recipe.components.chip.LoadingRankChip
import com.me.recipe.ui.recipe.components.chip.RankChip
import com.me.recipe.ui.recipe.components.shimmer.LoadingRecipeShimmer
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.compose.OnClick
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun RecipeContent(
    recipe: Recipe?,
    isLoading: Boolean,
    onLikeClicked: OnClick,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        TitleRow(
            title = recipe?.title,
            rank = recipe?.rating,
            isLoading = isLoading,
            onLikeClicked = onLikeClicked,
        )
        if (isLoading) {
            LoadingRecipeShimmer()
        } else if (recipe != null) {
            RecipeInfoView(
                dateUpdated = recipe.date,
                publisher = recipe.publisher,
                ingredients = recipe.ingredients,
            )
        }
    }
}

@Composable
private fun TitleRow(
    title: String?,
    rank: String?,
    isLoading: Boolean,
    onLikeClicked: OnClick,
) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("testTag_TitleRow"),
    ) {
        if (!title.isNullOrEmpty()) {
            TitleText(title)
        }
        if (!rank.isNullOrEmpty()) {
            RankChip(rank = rank, onLikeClicked = onLikeClicked)
        } else if (isLoading) {
            LoadingRankChip()
        }
    }
}

@Composable
private fun RowScope.TitleText(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .wrapContentWidth(Alignment.Start)
            .weight(1f)
            .testTag("testTag_TitleRow_Text"),
    )
}

@Composable
internal fun RecipeInfoView(
    dateUpdated: String,
    publisher: String,
    ingredients: ImmutableList<String>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.testTag("testTag_RecipeInfoView"),
    ) {
        Text(
            text = stringResource(R.string.recipe_date_and_publisher, dateUpdated, publisher),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("testTag_RecipeInfoView_Text"),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        HorizontalDivider(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .testTag("testTag_RecipeInfoView_HorizontalDivider"),
        )
        for (ingredient in ingredients) {
            Text(
                text = ingredient,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
                    .testTag("testTag_ingredient_Text"),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Preview
@Composable
private fun RecipeContentPreview() {
    RecipeTheme(true) {
        RecipeContent(
            recipe = Recipe.testData(),
            isLoading = false,
            onLikeClicked = {},
        )
    }
}
