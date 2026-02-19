package com.me.recipe.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
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
        var authMode by rememberRetained { mutableStateOf(AuthMode.LOGIN) }
        var email by rememberRetained { mutableStateOf("") }
        var password by rememberRetained { mutableStateOf("") }
        var retryPassword by rememberRetained { mutableStateOf("") }
        var hasPasswordError by rememberRetained { mutableStateOf(false) }

        return AuthState(
            authMode = authMode,
            email = email,
            password = password,
            retryPassword = retryPassword,
            hasPasswordError = hasPasswordError,
            eventSink = { event ->
                when(event) {
                    is AuthEvent.OnEmailChange -> email = event.email
                    is AuthEvent.OnPasswordChange -> {
                        password = event.password
                        hasPasswordError = false
                    }
                    is AuthEvent.OnRetryPasswordChange -> {
                        retryPassword = event.password
                        hasPasswordError = false
                    }
                    AuthEvent.OnSwitchModeClicked -> {
                        hasPasswordError = false
                        password = ""
                        retryPassword = ""
                        authMode =
                            if (authMode == AuthMode.LOGIN) AuthMode.REGISTER else AuthMode.LOGIN
                    }

                    AuthEvent.OnSubmitClicked -> {
                        if (authMode == AuthMode.REGISTER && password != retryPassword) {
                            hasPasswordError = true
                        } else {
                            //todo
                        }
                    }
                }
            },
        )
    }
}

@CircuitInject(AuthScreen::class, SingletonComponent::class)
@AssistedFactory
interface Factory {
    fun create(navigator: Navigator): AuthPresenter
}
