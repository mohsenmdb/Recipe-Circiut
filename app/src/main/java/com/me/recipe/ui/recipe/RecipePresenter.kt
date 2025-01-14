package com.me.recipe.ui.recipe

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipe.usecases.GetRecipeUsecase
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import dagger.Lazy
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import timber.log.Timber

class RecipePresenter @AssistedInject constructor(
    @Assisted private val screen: RecipeUiScreen,
    @Assisted internal val navigator: Navigator,
    private val getRecipeUsecase: Lazy<GetRecipeUsecase>,
) : Presenter<RecipeUiState> {

    @Composable
    override fun present(): RecipeUiState {
        val stableScope = rememberStableCoroutineScope()

        LaunchedEffect(key1 = Unit) {
            getRecipeUsecase.get().invoke(GetRecipeUsecase.Params(recipeId = screen.itemId, uid = screen.itemUid))
        }

        val recipe: Result<Recipe>? by getRecipeUsecase.get().flow.collectAsState(initial = null)
        Timber.d("RecipePresenter recipe = ${recipe?.getOrNull()}")

        navigator.toString()
        return RecipeUiState(
            recipe = recipe?.getOrNull(),
            exception = recipe?.exceptionOrNull(),
            eventSink = { event ->
                when (event) {
                    RecipeUiEvent.OnPlayClicked -> {}
                }
            },
        )
    }
}

@CircuitInject(RecipeUiScreen::class, SingletonComponent::class)
@AssistedFactory
interface Factory {
    fun create(screen: RecipeUiScreen, navigator: Navigator): RecipePresenter
}
