package com.me.recipe.ui.recipelist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.compose.OnClick

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecipeListAppBar(
    category: String,
    onBackPress: OnClick,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = {
            Text(
                text = category,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.clickable {
                    onBackPress.invoke()
                },
            )
        },
    )
}

@Preview
@Composable
private fun RecipeListAppBarPreview() {
    RecipeTheme(false) {
        RecipeListAppBar(
            category = "Beef",
            onBackPress = {},
        )
    }
}

@Preview
@Composable
private fun HomeAppBarDarkPreview() {
    RecipeTheme(true) {
        RecipeListAppBar(
            category = "Beef",
            onBackPress = {},
        )
    }
}
