package com.me.recipe.ui.profile

import androidx.compose.runtime.Stable
import com.me.recipe.ui.component.util.UiMessage
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data object ProfileScreen : Screen

typealias ProfileEventSink = (ProfileEvent) -> Unit

@Stable
data class ProfileState(
    val message: UiMessage? = null,
    val eventSink: ProfileEventSink,
) : CircuitUiState

sealed interface ProfileEvent : CircuitUiEvent {
    data object OnSubmitClicked : ProfileEvent
    data object ClearMessage : ProfileEvent
}
