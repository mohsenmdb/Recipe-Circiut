package com.me.recipe.ui.profile.userInfo

import androidx.compose.runtime.Stable
import com.me.recipe.shared.datastore.UserInfo
import com.me.recipe.ui.component.util.UiMessage
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data object UserInfoScreen : Screen

typealias UserInfoEventSink = (UserInfoEvent) -> Unit

@Stable
data class UserInfoState(
    val message: UiMessage? = null,
    val eventSink: UserInfoEventSink,
    val userInfo: UserInfo,
) : CircuitUiState

sealed interface UserInfoEvent : CircuitUiEvent {
    data object OnLogoutClicked : UserInfoEvent
    data object ClearMessage : UserInfoEvent
}
