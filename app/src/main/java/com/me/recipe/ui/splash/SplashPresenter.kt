package com.me.recipe.ui.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.me.recipe.ui.main.MainScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay

class SplashPresenter @AssistedInject constructor(
    @Assisted internal val navigator: Navigator,
) : Presenter<SplashState> {

    @Composable
    override fun present(): SplashState {
        LaunchedEffect(Unit) {
            delay(2000)
            navigator.resetRoot(MainScreen)
        }
        return SplashState(
            eventSink = {},
        )
    }
}

@CircuitInject(SplashScreen::class, SingletonComponent::class)
@AssistedFactory
interface Factory {
    fun create(navigator: Navigator): SplashPresenter
}
