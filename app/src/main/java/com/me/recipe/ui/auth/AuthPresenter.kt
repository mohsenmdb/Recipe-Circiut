package com.me.recipe.ui.auth

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.me.recipe.BuildConfig
import com.me.recipe.R
import com.me.recipe.domain.features.auth.usecase.LoginUseCase
import com.me.recipe.domain.features.auth.usecase.RegisterUseCase
import com.me.recipe.domain.features.model.User
import com.me.recipe.shared.datastore.UserDataStore
import com.me.recipe.shared.datastore.UserInfo
import com.me.recipe.ui.component.util.UiMessage
import com.me.recipe.ui.component.util.UiMessageManager
import com.me.recipe.ui.profile.ProfileScreen
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
    private val registerUseCase: Lazy<RegisterUseCase>,
    private val userDataStore: Lazy<UserDataStore>,
    private val errorFormatter: Lazy<ErrorFormatter>,
) : Presenter<AuthState> {

    @Composable
    override fun present(): AuthState {
        val scope = rememberStableCoroutineScope()
        val uiMessageManager = rememberRetained { UiMessageManager() }
        val message by uiMessageManager.message.collectAsRetainedState(null)
        var authMode by rememberRetained { mutableStateOf(AuthMode.LOGIN) }
        var username by rememberRetained { mutableStateOf("") }
        var password by rememberRetained { mutableStateOf("") }
        var firstName by rememberRetained { mutableStateOf("") }
        var lastName by rememberRetained { mutableStateOf("") }
        var age by rememberRetained { mutableStateOf("") }
        var retryPassword by rememberRetained { mutableStateOf("") }
        var hasPasswordError by rememberRetained { mutableStateOf(false) }
        var isLoading by rememberRetained { mutableStateOf(false) }

        suspend fun createMessage(message: String? = null, @StringRes messageRes: Int? = null) {
            if (message != null) {
                uiMessageManager.emitMessage(UiMessage.createSnackbar(message))
            } else if (messageRes != null) {
                uiMessageManager.emitMessage(UiMessage.createSnackbar(messageRes))
            }
        }

        suspend fun saveUserInfo(accessToken: String, user: User) {
            userDataStore.get().setUser(
                UserInfo(
                    accessToken = accessToken,
                    username = user.username,
                    firstName = user.firstName,
                    lastName = user.lastName,
                    age = user.age.toString(),
                ),
            )
        }

        suspend fun loginUser() {
            isLoading = true
            if (BuildConfig.DEBUG) delay(500)
            loginUseCase.get().invoke(LoginUseCase.Params(username, password)).apply {
                getOrNull()?.let {
                    if (it.accessToken.isNotEmpty()) {
                        saveUserInfo(it.accessToken, it.user)
                        createMessage(messageRes = R.string.login_successful)
                        delay(500)
                        navigator.resetRoot(ProfileScreen)
                    } else {
                        createMessage(messageRes = R.string.server_error_retry)
                    }
                }
                exceptionOrNull()?.let {
                    createMessage(errorFormatter.get().format(it))
                }
            }
            isLoading = false
        }

        suspend fun registerUser() {
            isLoading = true
            if (BuildConfig.DEBUG) delay(500)
            registerUseCase.get().invoke(
                RegisterUseCase.Params(
                    username = username,
                    firstName = firstName,
                    lastName = lastName,
                    age = age.toIntOrNull(),
                    password = password,
                ),
            ).apply {
                getOrNull()?.let {
                    if (it.accessToken.isNotEmpty()) {
                        saveUserInfo(it.accessToken, it.user)
                        createMessage(messageRes = R.string.register_successful)
                        delay(500)
                        navigator.resetRoot(ProfileScreen)
                    } else {
                        createMessage(messageRes = R.string.server_error_retry)
                    }
                }
                exceptionOrNull()?.let {
                    createMessage(errorFormatter.get().format(it))
                }
            }
            isLoading = false
        }

        return AuthState(
            isLoading = isLoading,
            authMode = authMode,
            username = username,
            firstName = firstName,
            lastName = lastName,
            age = age,
            password = password,
            retryPassword = retryPassword,
            hasPasswordError = hasPasswordError,
            message = message,
            eventSink = { event ->
                when (event) {
                    is AuthEvent.OnUsernameChange -> username = event.username
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
                        when (authMode) {
                            AuthMode.REGISTER -> {
                                if (password != retryPassword) {
                                    hasPasswordError = true
                                    return@AuthState
                                } else {
                                    scope.launch { registerUser() }
                                }
                            }

                            AuthMode.LOGIN -> {
                                scope.launch { loginUser() }
                            }

                            else -> {}
                        }
                    }
                    is AuthEvent.OnFirstNameChange -> firstName = event.firstName
                    is AuthEvent.OnLastNameChange -> lastName = event.lastName
                    is AuthEvent.OnAgeChange -> {
                        event.age.toIntOrNull() ?: return@AuthState
                        age = event.age
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
