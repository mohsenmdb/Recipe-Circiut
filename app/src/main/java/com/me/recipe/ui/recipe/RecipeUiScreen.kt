package com.me.recipe.ui.recipe

import androidx.compose.runtime.Stable
import com.me.recipe.domain.features.recipe.model.CategoryRecipe
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.component.util.GenericDialogInfo
import com.me.recipe.ui.home.HomeContract.State
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuitx.android.AndroidScreen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeUiScreen(
    val itemId: Int,
    val itemUid: String,
    val itemTitle: String? = null,
    val itemImage: String? = null,
) : Screen

typealias RecipeUiSink = (RecipeUiEvent) -> Unit

@Stable
data class RecipeUiState(
    val recipe: Recipe? = null,
    val message: String? = null,
    val exception: Throwable? = null,
    val recipesLoading: Boolean = recipe == null && exception == null,
    val eventSink: RecipeUiSink,
) : CircuitUiState {
    companion object {
        fun testData() = RecipeUiState(
            recipe = Recipe.testData(),
            eventSink = {}
        )
    }
}

sealed interface RecipeUiEvent : CircuitUiEvent {

    data object OnPlayClicked : RecipeUiEvent
}
