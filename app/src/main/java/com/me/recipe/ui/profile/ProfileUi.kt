package com.me.recipe.ui.profile

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.me.recipe.ui.component.util.DefaultSnackbar
import com.me.recipe.ui.component.util.MessageEffect
import com.me.recipe.ui.theme.RecipeTheme
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent

@CircuitInject(ProfileScreen::class, SingletonComponent::class)
@Composable
internal fun ProfileUi(
    state: ProfileState,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val containerColor by animateColorAsState(
        targetValue = MaterialTheme.colorScheme.background,
        animationSpec = spring(stiffness = Spring.StiffnessVeryLow),
    )
    MessageEffect(
        snackbarHostState = snackbarHostState,
        uiMessage = state.message,
        onClearMessage = { state.eventSink(ProfileEvent.ClearMessage) },
    )

    Scaffold(
        containerColor = containerColor,
        snackbarHost = {
            DefaultSnackbar(
                snackbarHostState = snackbarHostState,
                onAction = { snackbarHostState.currentSnackbarData?.performAction() },
            )
        },
    ) { padding ->
        Content(modifier.padding(padding))
    }
}

@Composable
fun Content(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Text(text = "this is profile page")
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    RecipeTheme(true) {
        ProfileUi(
            state = ProfileState(eventSink = {}),
        )
    }
}
