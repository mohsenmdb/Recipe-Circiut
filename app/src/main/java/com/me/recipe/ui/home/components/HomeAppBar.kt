@file:OptIn(ExperimentalMaterial3Api::class)

package com.me.recipe.ui.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.me.recipe.R
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.compose.OnClick

@Composable
internal fun HomeAppBar(
    isDark: Boolean,
    onToggleTheme: OnClick,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        title = { AppNameText() },
        actions = {
            ChangeThemeIcon(isDark = isDark, onClick = onToggleTheme)
        },
    )
}

@Composable
private fun AppNameText() {
    Text(
        text = stringResource(id = R.string.app_name),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
    )
}

@Composable
private fun ChangeThemeIcon(isDark: Boolean, onClick: OnClick) {
    Icon(
        painter = painterResource(id = if (isDark) R.drawable.ic_light_mode_24 else R.drawable.ic_night_mode_24),
        contentDescription = stringResource(id = R.string.app_name),
        tint = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag("testTag_changeThemeIcon"),
    )
}

@Preview
@Composable
private fun HomeAppBarPreview() {
    RecipeTheme(false) {
        HomeAppBar(
            isDark = false,
            onToggleTheme = {},
        )
    }
}

@Preview
@Composable
private fun HomeAppBarDarkPreview() {
    RecipeTheme(true) {
        HomeAppBar(
            isDark = true,
            onToggleTheme = {},
        )
    }
}
