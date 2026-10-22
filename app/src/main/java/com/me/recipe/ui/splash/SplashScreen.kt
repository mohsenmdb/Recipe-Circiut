package com.me.recipe.ui.splash

import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.ParcelableScreen
import kotlinx.parcelize.Parcelize

@Parcelize
data object SplashScreen : ParcelableScreen

typealias SplashEventSink = (SplashEvent) -> Unit

@Stable
data class SplashState(
    val eventSink: SplashEventSink,
) : CircuitUiState

sealed interface SplashEvent : CircuitUiEvent
