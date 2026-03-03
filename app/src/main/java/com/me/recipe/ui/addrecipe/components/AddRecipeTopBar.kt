package com.me.recipe.ui.addrecipe.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.me.recipe.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeTopBar(modifier: Modifier = Modifier) {
    TopAppBar(
        title = { TitleText() }
    )
}
@Composable
fun TitleText(modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = stringResource(R.string.add_recipe),
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.titleLarge,
    )
}
