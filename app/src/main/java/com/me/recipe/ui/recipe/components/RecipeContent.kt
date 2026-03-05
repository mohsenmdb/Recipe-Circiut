package com.me.recipe.ui.recipe.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.me.recipe.R
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.recipe.components.chip.RankChip
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.compose.OnClick
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun RecipeContent(
    recipe: Recipe,
    onLikeClicked: OnClick,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("testTag_RecipeInfoView"),
    ) {
        TitleRow(
            title = recipe.title,
            rank = recipe.rating,
            onLikeClicked = onLikeClicked,
        )
        RecipeInfoView(
            dateUpdated = recipe.date,
            publisher = recipe.publisher,
            ingredients = recipe.ingredients,
            description = recipe.description,
        )
    }
}

@Composable
private fun TitleRow(
    title: String?,
    rank: String?,
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
internal fun ColumnScope.RecipeInfoView(
    dateUpdated: String,
    publisher: String,
    ingredients: ImmutableList<String>,
    description: String,
) {
    RecipeTimeInfo(dateUpdated = dateUpdated, publisher = publisher)

    IngredientsSection(ingredients)
    Spacer(Modifier.height(16.dp))
    DescriptionSection(description)
}

@Composable
private fun ColumnScope.DescriptionSection(description: String) {
    Text(
        text = stringResource(R.string.description),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(vertical = 6.dp),
    )
    Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun ColumnScope.RecipeTimeInfo(dateUpdated: String, publisher: String) {
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
}

@Composable
private fun ColumnScope.IngredientsSection(ingredients: ImmutableList<String>) {
    Text(
        text = stringResource(R.string.ingredients),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(vertical = 6.dp),
    )

    FlowRow(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ingredients.fastForEach { ingredient ->
            IngredientChip(text = ingredient)
        }
    }
}

@Composable
internal fun IngredientChip(text: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.primary,
        ),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .testTag("testTag_ingredient_Text"),
        )
    }
}

@Preview
@Composable
private fun RecipeContentPreview() {
    RecipeTheme(true) {
        RecipeContent(
            recipe = Recipe.testData(),
            onLikeClicked = {},
        )
    }
}
