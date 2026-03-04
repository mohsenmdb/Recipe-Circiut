package com.me.recipe.ui.addrecipe

import android.net.Uri
import androidx.compose.runtime.Stable
import com.me.recipe.ui.component.util.UiMessage
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize

@Parcelize
data object AddRecipeScreen : Screen

typealias AddRecipeEventSink = (AddRecipeEvent) -> Unit

@Stable
data class AddRecipeState(
    val message: UiMessage? = null,
    val title: String = "",
    val description: String = "",
    val ingredientText: String = "",
    val imageUri: Uri? = null,
    val isLoading: Boolean = false,
    val isSubmitEnabled: Boolean = false,
    val ingredientsList: ImmutableList<String> = persistentListOf(),
    val eventSink: AddRecipeEventSink,
) : CircuitUiState

sealed interface AddRecipeEvent : CircuitUiEvent {
    data object ClearMessage : AddRecipeEvent
    data class OnTitleChanged(val value: String) : AddRecipeEvent
    data class OnDescriptionChanged(val value: String) : AddRecipeEvent
    data class OnIngredientsChanged(val value: String) : AddRecipeEvent
    data class OnImageSelected(val uri: Uri?) : AddRecipeEvent
    data object OnAddIngredientsClicked : AddRecipeEvent
    data class OnRemoveIngredientsClicked(val item: String) : AddRecipeEvent
    data object OnSubmitClicked : AddRecipeEvent
}
