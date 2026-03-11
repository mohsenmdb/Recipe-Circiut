package com.me.recipe.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.me.recipe.domain.features.auth.usecase.GetLoginStateUseCase
import com.me.recipe.shared.datastore.LoginState
import com.me.recipe.ui.main.navigation.HomeTab
import com.me.recipe.ui.main.navigation.NavigationTabs
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.retained.rememberRetained
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
        var selectedTab by rememberRetained { mutableStateOf<NavigationTabs>(HomeTab) }
        val loginState by getLoginStateUseCase.get().flow.collectAsRetainedState(initial = null)
        LaunchedEffect(Unit) {
            getLoginStateUseCase.get().invoke(Any())
        }

        return MainState(
            isUserLoggedIn = loginState is LoginState.LoggedIn,
            selectedTab = selectedTab,
            eventSink = { event ->
                when (event) {
                    is MainEvent.OnSelectedTabChanged -> selectedTab = event.tab
                }
            },
        )
    }
}

@CircuitInject(MainScreen::class, SingletonComponent::class)
@AssistedFactory
interface Factory {
    fun create(navigator: Navigator): MainPresenter
}
