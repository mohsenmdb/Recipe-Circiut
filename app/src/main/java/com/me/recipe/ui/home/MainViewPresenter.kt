package com.me.recipe.ui.home

import androidx.compose.runtime.Composable
import com.me.recipe.domain.features.recipe.model.CategoryRecipe
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.home.MainUiEvent.OnPlayClicked
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import kotlinx.collections.immutable.persistentListOf

class MainViewPresenter @AssistedInject constructor(
    @Assisted private val screen: MainUiScreen,
    @Assisted internal val navigator: Navigator,
) : Presenter<MainUiState> {

    @Composable
    override fun present(): MainUiState {
        val stableScope = rememberStableCoroutineScope()

        navigator.toString()
        return MainUiState(
            sliderRecipes = persistentListOf(Recipe.testData()),
            categoriesRecipes = persistentListOf(CategoryRecipe.testData()),
            eventSink = { event ->
                when (event) {
                    OnPlayClicked -> {}
                }
            },
        )
    }
}

@CircuitInject(MainUiScreen::class, SingletonComponent::class)
@AssistedFactory
interface Factory {
    fun create(screen: MainUiScreen, navigator: Navigator): MainViewPresenter
}
