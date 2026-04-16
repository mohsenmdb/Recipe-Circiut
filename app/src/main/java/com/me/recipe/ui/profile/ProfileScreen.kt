package com.me.recipe.ui.profile

import androidx.compose.runtime.Stable
import com.me.recipe.R
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.parcelize.Parcelize

@Parcelize
data object ProfileScreen : Screen

typealias ProfileEventSink = (ProfileEvent) -> Unit

@Stable
data class ProfileState(
    val tabs: ImmutableList<ProfileTabs> = profileTabs,
    val selectedTab: ProfileTabs = UserInfoTab,
    val eventSink: ProfileEventSink,
) : CircuitUiState {
    companion object {
        val profileTabs = persistentListOf(UserInfoTab, MyRecipesTab)
    }
}

sealed interface ProfileEvent : CircuitUiEvent {
    data class OnTabClick(val tab: ProfileTabs) : ProfileEvent
}

interface ProfileTabs {
    val titleRes: Int
}

object UserInfoTab : ProfileTabs {
    override val titleRes = R.string.tab_user_info
}

object MyRecipesTab : ProfileTabs {
    override val titleRes = R.string.tab_my_recipes
}
