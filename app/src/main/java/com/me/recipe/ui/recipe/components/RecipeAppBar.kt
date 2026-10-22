package com.me.recipe.ui.recipe.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.compose.OnClick

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RecipeAppBar(
    title: String,
    onBackPress: OnClick,
    modifier: Modifier = Modifier,
) {
    CenterAlignedTopAppBar(
        modifier = modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackPress) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        },
    )
}

@Preview
@Composable
private fun RecipeAppBarPreview() {
    RecipeTheme(false) {
        RecipeAppBar(
            title = "Pizza Potato Skins",
            onBackPress = {},
        )
    }
}

@Preview
@Composable
private fun RecipeAppBarDarkPreview() {
    RecipeTheme(true) {
        RecipeAppBar(
            title = "Pizza Potato Skins",
            onBackPress = {},
        )
    }
}
