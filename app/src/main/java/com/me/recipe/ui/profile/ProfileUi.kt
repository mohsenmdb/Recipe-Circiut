package com.me.recipe.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.me.recipe.ui.profile.myrecipes.MyRecipesScreen
import com.me.recipe.ui.profile.userInfo.UserInfoScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.CircuitContent
import dagger.hilt.components.SingletonComponent
import kotlinx.collections.immutable.ImmutableList

@CircuitInject(ProfileScreen::class, SingletonComponent::class)
@Composable
internal fun ProfileUi(
    state: ProfileState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        TabsRow(
            selectedTab = state.tabs.indexOf(state.selectedTab),
            tabs = state.tabs,
            onTabClick = { state.eventSink(ProfileEvent.OnTabClick(it)) },
        )
        when (state.selectedTab) {
            UserInfoTab -> UserInfoContent()
            MyRecipesTab -> MyRecipesContent()
        }
    }
}

@Composable
private fun TabsRow(selectedTab: Int, tabs: ImmutableList<ProfileTabs>, onTabClick: (ProfileTabs) -> Unit) {
    PrimaryTabRow(selectedTabIndex = selectedTab) {
        tabs.forEachIndexed { index, tab ->
            Tab(
                selected = selectedTab == index,
                onClick = { onTabClick(tab) },
                text = { Text(stringResource(tab.titleRes)) },
            )
        }
    }
}

@Composable
fun UserInfoContent(modifier: Modifier = Modifier) {
    CircuitContent(modifier = modifier.fillMaxSize(), screen = UserInfoScreen, onNavEvent = {})
}

@Composable
fun MyRecipesContent(modifier: Modifier = Modifier) {
    CircuitContent(modifier = modifier.fillMaxSize(), screen = MyRecipesScreen, onNavEvent = {})
}
