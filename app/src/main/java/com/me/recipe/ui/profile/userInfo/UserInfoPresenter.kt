package com.me.recipe.ui.profile.userInfo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.me.recipe.R
import com.me.recipe.domain.features.auth.usecase.GetLoginStateUseCase
import com.me.recipe.shared.datastore.LoginState
import com.me.recipe.shared.datastore.UserDataStore
import com.me.recipe.shared.datastore.UserInfo
import com.me.recipe.ui.auth.AuthScreen
import com.me.recipe.ui.component.util.UiMessage
import com.me.recipe.ui.component.util.UiMessageManager
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
import kotlinx.coroutines.launch

class UserInfoPresenter @AssistedInject constructor(
    @Assisted internal val navigator: Navigator,
    private val getLoginStateUseCase: Lazy<GetLoginStateUseCase>,
    private val userDataStore: Lazy<UserDataStore>,
) : Presenter<UserInfoState> {

    @Composable
    override fun present(): UserInfoState {
        val scope = rememberStableCoroutineScope()
        var userInfo by rememberRetained { mutableStateOf(UserInfo()) }
        val uiMessageManager = rememberRetained { UiMessageManager() }
        val message by uiMessageManager.message.collectAsRetainedState(null)
        val loginState by getLoginStateUseCase.get().flow.collectAsRetainedState(initial = null)

        LaunchedEffect(Unit) {
            getLoginStateUseCase.get().invoke(Any())
        }
        LaunchedEffect(loginState) {
            if (loginState is LoginState.LoggedIn) {
                userInfo = (loginState as LoginState.LoggedIn).user
            }
        }
        return UserInfoState(
            userInfo = userInfo,
            message = message,
            eventSink = { event ->
                when (event) {
                    UserInfoEvent.ClearMessage -> scope.launch { uiMessageManager.clearMessage() }
                    UserInfoEvent.OnLogoutClicked -> scope.launch {
                        userDataStore.get().logout()
                        uiMessageManager.emitMessage(UiMessage.createToast(R.string.logout))
                        //todo fix me i am not working
                        navigator.resetRoot(AuthScreen)
                    }
                }
            },
        )
    }
}

@CircuitInject(UserInfoScreen::class, SingletonComponent::class)
@AssistedFactory
interface Factory {
    fun create(navigator: Navigator): UserInfoPresenter
}
