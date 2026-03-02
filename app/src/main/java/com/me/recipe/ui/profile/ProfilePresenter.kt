package com.me.recipe.ui.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.me.recipe.R
import com.me.recipe.ui.component.util.UiMessage
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

class ProfilePresenter @AssistedInject constructor(
    @Assisted internal val navigator: Navigator,
) : Presenter<ProfileState> {

    @Composable
    override fun present(): ProfileState {
        val scope = rememberStableCoroutineScope()
        val uiMessageManager = rememberRetained { UiMessageManager() }
        val message by uiMessageManager.message.collectAsRetainedState(null)

        return ProfileState(
            message = message,
            eventSink = { event ->
                when (event) {
                    ProfileEvent.ClearMessage -> scope.launch { uiMessageManager.clearMessage() }
                    ProfileEvent.OnSubmitClicked -> scope.launch {
                        uiMessageManager.emitMessage(UiMessage.createSnackbar(R.string.login_successful))
                    }
                }
            },
        )
    }
}

@CircuitInject(ProfileScreen::class, SingletonComponent::class)
@AssistedFactory
interface Factory {
    fun create(navigator: Navigator): ProfilePresenter
}
