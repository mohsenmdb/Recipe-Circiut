package com.me.recipe.ui.recipe

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipe.usecases.GetRecipeUsecase
import com.me.recipe.ui.component.util.UiMessage
import com.me.recipe.ui.component.util.UiMessageManager
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import dagger.Lazy
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RecipePresenter @AssistedInject constructor(
    @Assisted private val screen: RecipeUiScreen,
    @Assisted internal val navigator: Navigator,
    private val getRecipeUsecase: Lazy<GetRecipeUsecase>,
) : Presenter<RecipeUiState> {

    @Composable
    override fun present(): RecipeUiState {
        val stableScope = rememberStableCoroutineScope()
        val uiMessageManager = remember { UiMessageManager() }
        val message by uiMessageManager.message.collectAsState(null)

        LaunchedEffect(key1 = Unit) {
            getRecipeUsecase.get()
                .invoke(GetRecipeUsecase.Params(recipeId = screen.itemId, uid = screen.itemUid))
        }
        val recipeResult: Result<Recipe>? by getRecipeUsecase.get().flow.collectAsState(initial = null)
        val recipe = recipeResult?.getOrNull()
        LaunchedEffect(key1 = recipe?.title) {
            if (recipe?.title != null && recipe.title.isNotEmpty()) {
                uiMessageManager.emitMessage(UiMessage.createToast(recipe.title))
            }
        }
        navigator.toString()
        return RecipeUiState(
            recipe = recipe,
            message = message,
            exception = recipeResult?.exceptionOrNull(),
            eventSink = { event ->
                when (event) {
                    RecipeUiEvent.ClearMessage ->
                        stableScope.launch { uiMessageManager.clearMessage() }

                    RecipeUiEvent.OnLikeClicked -> {
                        if (recipe?.rating != null) {
                            stableScope.launch {
                                uiMessageManager.emitMessage(UiMessage.createSnackbar(recipe.rating))
                            }
                        }
                    }
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
