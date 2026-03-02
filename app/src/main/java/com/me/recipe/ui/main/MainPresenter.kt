package com.me.recipe.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.me.recipe.domain.features.auth.usecase.GetLoginStateUseCase
import com.me.recipe.shared.datastore.LoginState
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.Lazy
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent

class MainPresenter @AssistedInject constructor(
    @Assisted internal val navigator: Navigator,
    private val getLoginStateUseCase: Lazy<GetLoginStateUseCase>,
) : Presenter<MainState> {

    @Composable
    override fun present(): MainState {
        val loginState by getLoginStateUseCase.get().flow.collectAsRetainedState(initial = null)
        LaunchedEffect(Unit) {
            getLoginStateUseCase.get().invoke(Any())
        }

        return MainState(
            isUserLoggedIn = loginState is LoginState.LoggedIn,
            eventSink = {},
        )
    }
}

@CircuitInject(MainScreen::class, SingletonComponent::class)
@AssistedFactory
interface Factory {
    fun create(navigator: Navigator): MainPresenter
}
