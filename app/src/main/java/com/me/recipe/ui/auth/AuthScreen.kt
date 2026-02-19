package com.me.recipe.ui.auth

import androidx.compose.runtime.Stable
import com.me.recipe.ui.component.util.UiMessage
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data object AuthScreen : Screen

typealias AuthEventSink = (AuthEvent) -> Unit

@Stable
data class AuthState(
    val email: String = "",
    val password: String = "",
    val retryPassword: String = "",
    val message: UiMessage? = null,
    val hasPasswordError: Boolean = false,
    val isLoading: Boolean = false,
    val authMode: AuthMode = AuthMode.LOGIN,
    val eventSink: AuthEventSink,
) : CircuitUiState

sealed interface AuthEvent : CircuitUiEvent {
    data class OnEmailChange(val email: String) : AuthEvent
    data class OnPasswordChange(val password: String) : AuthEvent
    data class OnRetryPasswordChange(val password: String) : AuthEvent
    data object OnSwitchModeClicked : AuthEvent
    data object OnSubmitClicked : AuthEvent
    data object ClearMessage : AuthEvent
}
enum class AuthMode {
    LOGIN,
    REGISTER,
}

val AuthState.isLoginMode get() = authMode == AuthMode.LOGIN
