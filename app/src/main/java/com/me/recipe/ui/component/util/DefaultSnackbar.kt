package com.me.recipe.ui.component.util

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.me.recipe.R

@Composable
internal fun DefaultSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit? = {},
    onAction: () -> Unit? = {},
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
                        ActionButton(onClicked = onAction, actionLabel = actionLabel)
                    }
                },
                dismissAction = {
                    if (data.visuals.withDismissAction) {
                        ActionButton(
                            actionLabel = stringResource(R.string.ok),
                            onClicked = {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                onDismiss()
                            },
                        )
                    }
                },
            )
        },
        modifier = modifier,
    )
}

@Composable
private fun ActionButton(onClicked: () -> Unit?, actionLabel: String) {
    TextButton(onClick = { onClicked() }) {
        Text(
            text = actionLabel,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
fun MessageEffect(
    snackbarHostState: SnackbarHostState,
    message: UiMessage?,
    onClearMessage: () -> Unit,
    onActionClicked: () -> Unit = {},
) {
    val context = LocalContext.current
    var showMessage: UiMessage? by remember { mutableStateOf(null) }
    var actionOk by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(showMessage) {
        when (showMessage?.message) {
            is Message.Snackbar -> {
                snackbarHostState.currentSnackbarData?.dismiss()
                val result = snackbarHostState.showSnackbar(
                    message = showMessage!!.message.text,
                    actionLabel = actionOk,
                    withDismissAction = true,
                )
                if (result == SnackbarResult.ActionPerformed) {
                    onActionClicked()
                }
                showMessage = null
            }

            is Message.Toast -> {
                toast(context, showMessage!!.message.text)
            }
            else -> {}
        }
    }
    message?.let {
        actionOk = message.actionText.takeIf { it != null }?.let { stringResource(id = it) }
        showMessage = it
        onClearMessage()
    }
}
