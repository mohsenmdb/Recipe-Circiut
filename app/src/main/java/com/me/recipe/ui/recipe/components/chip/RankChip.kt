package com.me.recipe.ui.recipe.components.chip

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.compose.OnClick

@Composable
internal fun RankChip(
    rank: String,
    onLikeClicked: OnClick,
    modifier: Modifier = Modifier,
) {
    AssistChip(
        shape = CircleShape,
        onClick = onLikeClicked,
        label = {
            Text(
                text = rank,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        leadingIcon = {
            Icon(
                Icons.Filled.ThumbUp,
                contentDescription = "ThumbUp Icon",
                modifier
                    .size(AssistChipDefaults.IconSize)
                    .testTag("testTag_RankChip"),
                tint = Color.Red,
            )
        },
    )
}

@Preview
@Composable
private fun RankChipPreview() {
    RecipeTheme(true) {
        RankChip(rank = "1", onLikeClicked = {})
    }
}
