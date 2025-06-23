package com.me.recipe.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.me.recipe.ui.home.HomeUiScreen
import com.me.recipe.ui.search.SearchScreen
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.screen.Screen

@Composable
internal fun NavBottomBar(
    selectedIndex: Int,
    onIndexChanged: (Int) -> Unit,
    navigator: Navigator,
) {
    val itemColors = NavigationBarItemDefaults.colors(
        indicatorColor = MaterialTheme.colorScheme.tertiary,
        selectedIconColor = MaterialTheme.colorScheme.surface,
        selectedTextColor = MaterialTheme.colorScheme.onSurface,
        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    AnimatedVisibility(
        visible = isHomeDestination(navigator.peek()),
        enter = expandVertically(),
        exit = shrinkVertically(),
    ) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.fillMaxWidth(),
        ) {
            bottomNavigationScreens.forEachIndexed { index, tab ->
                NavigationBarItem(
                    icon = { Icon(imageVector = tab.icon!!, contentDescription = null) },
                    label = { Text(stringResource(tab.titleRes)) },
                    selected = selectedIndex == index,
                    colors = itemColors,
                    onClick = {
                        onIndexChanged(index)
                        navigator.resetRoot(
                            newRoot = getScreenForTab(tab),
                            saveState = true,
                            restoreState = true,
                        )
                    },
                )
            }
        }
    }
}

private fun getScreenForTab(tab: NavigationDestination) = when (tab) {
    HomeDestination -> HomeUiScreen()
    SearchDestination -> SearchScreen()
    else -> HomeUiScreen()
}

private fun isHomeDestination(screen: Screen?) =
    screen is HomeUiScreen || screen is SearchScreen
