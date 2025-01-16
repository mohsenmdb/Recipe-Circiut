package com.me.recipe.ui.search.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
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
import com.me.recipe.ui.theme.RecipeTheme

@Composable
internal fun AppendingLoadingView(
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .testTag("testTag_LoadingView_CircularProgressIndicator"),
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = stringResource(R.string.loading),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(top = 8.dp)
                .testTag("testTag_LoadingView_Text"),
        )
    }
}

@Preview
@Composable
private fun LoadingViewPreview() {
    RecipeTheme(true) {
        AppendingLoadingView()
    }
}
