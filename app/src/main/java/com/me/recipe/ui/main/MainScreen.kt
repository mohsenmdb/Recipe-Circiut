package com.me.recipe.ui.main

import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data object MainScreen : Screen

typealias MainEventSink = (MainEvent) -> Unit

@Stable
data class MainState(
    val eventSink: MainEventSink,
) : CircuitUiState

sealed interface MainEvent : CircuitUiEvent
