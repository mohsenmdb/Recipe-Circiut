package com.me.recipe.ui.today

import androidx.compose.runtime.Stable
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.component.util.UiMessage
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data object TodayRecipeScreen : Screen

typealias TodayEventSink = (TodayRecipeEvent) -> Unit

@Stable
data class TodayRecipeState(
    val recipe: Recipe,
    val message: UiMessage? = null,
    val exception: Throwable? = null,
    val isLoading: Boolean = false,
    val eventSink: TodayEventSink,
) : CircuitUiState {
    companion object {
        fun testData() = TodayRecipeState(
            recipe = Recipe.testData(),
            eventSink = {},
        )
    }
}

sealed interface TodayRecipeEvent : CircuitUiEvent {
    data object ClearMessage : TodayRecipeEvent
    data object OnLikeClicked : TodayRecipeEvent
}
