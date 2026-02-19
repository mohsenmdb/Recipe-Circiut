package com.me.recipe.ui.auth

import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data object AuthScreen : Screen

typealias AuthEventSink = (AuthEvent) -> Unit

@Stable
data class AuthState(
    val eventSink: AuthEventSink,
) : CircuitUiState

sealed interface AuthEvent : CircuitUiEvent
