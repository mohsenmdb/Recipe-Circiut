package com.me.recipe.ui.profile

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

class ProfilePresenter @AssistedInject constructor(
    @Assisted internal val navigator: Navigator,
) : Presenter<ProfileState> {

    @Composable
    override fun present(): ProfileState {
        var selectedTab by rememberRetained { mutableStateOf<ProfileTabs>(UserInfoTab) }

        return ProfileState(
            selectedTab = selectedTab,
            eventSink = { event ->
                when (event) {
                    is ProfileEvent.OnTabClick -> {
                        selectedTab = event.tab
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
