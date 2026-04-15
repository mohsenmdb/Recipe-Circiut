package com.me.recipe.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.R
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.compose.OnClick

@Composable
internal fun AppendingErrorView(
    message: String,
    onClick: OnClick,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        Text(
            text = message.ifEmpty { stringResource(R.string.server_error_retry) },
            style = MaterialTheme.typography.bodyLarge,
        )
        RetryButton(
            onClick = onClick,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

@Composable
internal fun RetryButton(
    onClick: OnClick,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier.padding(horizontal = 20.dp),
    ) {
        Text(
            text = stringResource(R.string.try_again),
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Preview
@Composable
private fun AppendingErrorViewPreview() {
    RecipeTheme(true) {
        AppendingErrorView(message = "Try Again", onClick = {})
    }
}
