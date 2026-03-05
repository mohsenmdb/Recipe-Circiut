package com.me.recipe.ui.recipe

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.me.recipe.BuildConfig
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipe.usecases.GetRecipeUseCase
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
    @Assisted private val screen: RecipeScreen,
    @Assisted internal val navigator: Navigator,
    private val getRecipeUseCase: Lazy<GetRecipeUseCase>,
) : Presenter<RecipeState> {

    @Composable
    override fun present(): RecipeState {
        val stableScope = rememberStableCoroutineScope()
        val uiMessageManager = remember { UiMessageManager() }
        val message by uiMessageManager.message.collectAsState(null)

        LaunchedEffect(key1 = Unit) {
            if (BuildConfig.DEBUG) delay(1000)
            getRecipeUseCase.get()
                .invoke(GetRecipeUseCase.Params(recipeId = screen.itemId, uid = screen.itemUid))
        }
        val recipeResult: Result<Recipe>? by getRecipeUseCase.get().flow.collectAsState(initial = null)
        val recipe = recipeResult?.getOrNull()
        return RecipeState(
            recipe = recipe ?: Recipe.EMPTY,
            message = message,
            exception = recipeResult?.exceptionOrNull(),
            isLoading = recipeResult?.exceptionOrNull() == null && recipe == null,
            eventSink = { event ->
                when (event) {
                    RecipeEvent.OnBackClicked -> navigator.pop()
                    RecipeEvent.ClearMessage ->
                        stableScope.launch { uiMessageManager.clearMessage() }

                    RecipeEvent.OnLikeClicked -> {
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

@CircuitInject(RecipeScreen::class, SingletonComponent::class)
@AssistedFactory
interface Factory {
    fun create(screen: RecipeScreen, navigator: Navigator): RecipePresenter
}
