package com.me.recipe.ui.component.util

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.me.recipe.R

@Composable
internal fun DefaultSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit?,
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(16.dp),
                content = {
                    Text(
                        text = data.visuals.message,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                action = {
                    data.visuals.actionLabel?.let { actionLabel ->
                        TextButton(
                            onClick = {
                                onDismiss()
                            },
                        ) {
                            Text(
                                text = actionLabel,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                },
            )
        },
        modifier = modifier,
    )
}

@Composable
fun SnackbarEffect(
    snackbarHostState: SnackbarHostState,
    message: UiMessage?,
    onClearMessage: () -> Unit,
) {
    val actionOk = stringResource(id = R.string.ok)
    var showMessage: UiMessage? by remember { mutableStateOf(null) }
    LaunchedEffect(showMessage) {
        when (showMessage?.message) {
            is Message.Snackbar -> {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(showMessage!!.message.text, actionOk)
                showMessage = null
            }

            is Message.Toast -> {}
            else -> {}
        }
    }
    message?.let {
        LaunchedEffect(message) {
            showMessage = it
            onClearMessage()
        }
    }
}
