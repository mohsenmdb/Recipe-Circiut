package com.me.recipe.ui.recipe

import androidx.compose.runtime.Stable
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.component.util.UiMessage
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeScreen(
    val itemId: Int,
    val itemUid: String,
    val itemTitle: String? = null,
    val itemImage: String? = null,
) : Screen

typealias RecipeEventSink = (RecipeEvent) -> Unit

@Stable
data class RecipeState(
    val recipe: Recipe? = null,
    val message: UiMessage? = null,
    val exception: Throwable? = null,
    val recipesLoading: Boolean = recipe == null && exception == null,
    val eventSink: RecipeEventSink,
) : CircuitUiState {
    companion object {
        fun testData() = RecipeState(
            recipe = Recipe.testData(),
            eventSink = {},
        )
    }
}

sealed interface RecipeEvent : CircuitUiEvent {
    data object OnBackClicked : RecipeEvent
    data object ClearMessage : RecipeEvent
    data object OnLikeClicked : RecipeEvent
}
