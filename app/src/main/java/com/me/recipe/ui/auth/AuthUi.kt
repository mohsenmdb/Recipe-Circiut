package com.me.recipe.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.slack.circuit.codegen.annotations.CircuitInject
import dagger.hilt.components.SingletonComponent

@CircuitInject(AuthScreen::class, SingletonComponent::class)
@Composable
internal fun AuthUi(
    state: AuthState,
    modifier: Modifier = Modifier,
) {
}
