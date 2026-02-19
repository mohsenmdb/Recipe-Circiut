package com.me.recipe.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.me.recipe.domain.features.auth.usecase.LoginUseCase
import com.me.recipe.shared.datastore.UserDataStore
import com.me.recipe.ui.component.util.UiMessage
import com.me.recipe.ui.component.util.UiMessageManager
import com.me.recipe.util.errorformater.ErrorFormatter
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
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

class AuthPresenter @AssistedInject constructor(
    @Assisted internal val navigator: Navigator,
    private val loginUseCase: Lazy<LoginUseCase>,
    private val userDataStore: Lazy<UserDataStore>,
    private val errorFormatter: Lazy<ErrorFormatter>,
) : Presenter<AuthState> {

    @Composable
    override fun present(): AuthState {
        val scope = rememberStableCoroutineScope()
        val uiMessageManager = rememberRetained { UiMessageManager() }
        val message by uiMessageManager.message.collectAsRetainedState(null)
        var authMode by rememberRetained { mutableStateOf(AuthMode.LOGIN) }
        var email by rememberRetained { mutableStateOf("") }
        var password by rememberRetained { mutableStateOf("") }
        var retryPassword by rememberRetained { mutableStateOf("") }
        var hasPasswordError by rememberRetained { mutableStateOf(false) }
        var isLoading by rememberRetained { mutableStateOf(false) }

        return AuthState(
            isLoading = isLoading,
            authMode = authMode,
            email = email,
            password = password,
            retryPassword = retryPassword,
            hasPasswordError = hasPasswordError,
            message = message,
            eventSink = { event ->
                when (event) {
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
                    AuthEvent.ClearMessage -> scope.launch { uiMessageManager.clearMessage() }
                    AuthEvent.OnSubmitClicked -> {
                        if (authMode == AuthMode.REGISTER && password != retryPassword) {
                            hasPasswordError = true
                            return@AuthState
                        }
                        if (authMode == AuthMode.LOGIN) {
                            scope.launch {
                                isLoading = true
                                delay(1000)
                                loginUseCase.get().invoke(LoginUseCase.Params(email, password)).apply {
                                    getOrNull()?.let {
                                        if (it.accessToken.isNotEmpty()) {
                                            userDataStore.get().setAccessToken(it.accessToken)
                                            uiMessageManager.emitMessage(UiMessage.createSnackbar("Login Successful"))
                                        }
                                    }
                                    exceptionOrNull()?.let {
                                        uiMessageManager.emitMessage(UiMessage.createSnackbar(errorFormatter.get().format(it)))
                                    }
                                }
                                isLoading = false
                            }
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
