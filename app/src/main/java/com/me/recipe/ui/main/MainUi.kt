package com.me.recipe.ui.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.me.recipe.ui.home.HomeScreen
import com.me.recipe.ui.main.navigation.NavBottomBar
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.runtime.Navigator
import com.slack.circuitx.gesturenavigation.GestureNavigationDecorationFactory
import dagger.hilt.components.SingletonComponent

@CircuitInject(MainScreen::class, SingletonComponent::class)
@Composable
internal fun MainUi(
    state: MainState,
    modifier: Modifier = Modifier,
) {
    val backstack = rememberSaveableBackStack(root = HomeScreen())
    val navigator = rememberCircuitNavigator(backstack)
    var selectedIndex by remember { mutableIntStateOf(0) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavBottomBar(
                selectedIndex = selectedIndex,
                onIndexChanged = { selectedIndex = it },
                navigator = navigator,
            )
        },
        content = { paddingValues ->
            Content(
                paddingValues = paddingValues,
                navigator = navigator,
                backstack = backstack,
            )
        },
    )
}

@Composable
private fun Content(
    paddingValues: PaddingValues,
    navigator: Navigator,
    backstack: SaveableBackStack,
) {
    val gestureNavigationDecoration = remember(navigator) {
        GestureNavigationDecorationFactory(onBackInvoked = navigator::pop)
    }

    NavigableCircuitContent(
        modifier = Modifier.padding(paddingValues),
        navigator = navigator,
        backStack = backstack,
        decoratorFactory = gestureNavigationDecoration,
    )
}
