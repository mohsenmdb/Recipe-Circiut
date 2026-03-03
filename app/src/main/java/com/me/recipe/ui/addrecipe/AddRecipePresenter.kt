package com.me.recipe.ui.addrecipe

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.me.recipe.ui.component.util.UiMessageManager
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.internal.rememberStableCoroutineScope
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.launch

class AddRecipePresenter @AssistedInject constructor(
    @Assisted internal val navigator: Navigator,
) : Presenter<AddRecipeState> {

    @Composable
    override fun present(): AddRecipeState {
        val scope = rememberStableCoroutineScope()
        val uiMessageManager = rememberRetained { UiMessageManager() }
        val message by uiMessageManager.message.collectAsRetainedState(null)

        return AddRecipeState(
            message = message,
            eventSink = { event ->
                when (event) {
                    AddRecipeEvent.ClearMessage -> scope.launch { uiMessageManager.clearMessage() }
                }
            },
        )
    }
}

@CircuitInject(AddRecipeScreen::class, SingletonComponent::class)
@AssistedFactory
interface Factory {
    fun create(navigator: Navigator): AddRecipePresenter
}
