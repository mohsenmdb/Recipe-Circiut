package com.me.recipe.ui.auth

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent

class AuthPresenter @AssistedInject constructor(
    @Assisted internal val navigator: Navigator,
) : Presenter<AuthState> {

    @Composable
    override fun present(): AuthState {
        return AuthState(
            eventSink = {},
        )
    }
}

@CircuitInject(AuthScreen::class, SingletonComponent::class)
@AssistedFactory
interface Factory {
    fun create(navigator: Navigator): AuthPresenter
}
