package com.me.recipe.ui.home

import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuitx.android.AndroidScreen
import kotlinx.parcelize.Parcelize

@Parcelize
data class MainUiScreen(
    val title: String? = null,
) : Screen

@Parcelize
data object HomeScreen1 : AndroidScreen

typealias MainUiSink = (MainUiEvent) -> Unit

@Stable
data class MainUiState(
    val uid: String,
    val eventSink: MainUiSink,
) : CircuitUiState

sealed interface MainUiEvent : CircuitUiEvent {

    data object OnPlayClicked : MainUiEvent
}
