package com.me.recipe.ui.home

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
data class MainUiScreen(
    val title: String? = null,
) : Screen

@Parcelize
data object HomeScreen1 : AndroidScreen

typealias MainUiSink = (MainUiEvent) -> Unit

@Stable
data class MainUiState(
    val sliderRecipes: ImmutableList<Recipe>? = null,
    val categoriesRecipes: ImmutableList<CategoryRecipe>? = null,
    val errors: GenericDialogInfo? = null,
    val sliderLoading: Boolean = sliderRecipes == null,
    val categoriesLoading: Boolean = categoriesRecipes == null,
    val isDark: Boolean = false,
    val eventSink: MainUiSink,
) : CircuitUiState {
    companion object {
        fun testData() = MainUiState(
            sliderRecipes = persistentListOf(Recipe.testData()),
            eventSink = {}
        )
    }
}

sealed interface MainUiEvent : CircuitUiEvent {

    data object OnPlayClicked : MainUiEvent
}


val MainUiState.showShimmer: Boolean
    get() = sliderLoading || categoriesLoading
