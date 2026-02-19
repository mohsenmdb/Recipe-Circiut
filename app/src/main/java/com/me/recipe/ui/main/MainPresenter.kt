package com.me.recipe.ui.main

import androidx.compose.runtime.Composable
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent

class MainPresenter @AssistedInject constructor(
    @Assisted internal val navigator: Navigator,
) : Presenter<MainState> {

    @Composable
    override fun present(): MainState {
        return MainState(
            eventSink = {},
        )
    }
}

@CircuitInject(MainScreen::class, SingletonComponent::class)
@AssistedFactory
interface Factory {
    fun create(navigator: Navigator): MainPresenter
}
