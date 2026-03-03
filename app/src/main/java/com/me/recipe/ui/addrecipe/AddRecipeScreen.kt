package com.me.recipe.ui.addrecipe

import androidx.compose.runtime.Stable
import com.me.recipe.ui.component.util.UiMessage
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data object AddRecipeScreen : Screen

typealias AddRecipeEventSink = (AddRecipeEvent) -> Unit

@Stable
data class AddRecipeState(
    val message: UiMessage? = null,
    val eventSink: AddRecipeEventSink,
) : CircuitUiState

sealed interface AddRecipeEvent : CircuitUiEvent {
    data object ClearMessage : AddRecipeEvent
}
