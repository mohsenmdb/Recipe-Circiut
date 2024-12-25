package com.me.recipe.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.me.recipe.shared.datastore.SettingsDataStore
import com.me.recipe.shared.datastore.UserDataStore
import com.me.recipe.ui.home.MainUiScreen
import com.me.recipe.ui.navigation.NavBottomBar
import com.me.recipe.ui.theme.RecipeTheme
import com.slack.circuit.backstack.SaveableBackStack
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.overlay.ContentWithOverlays
import com.slack.circuit.runtime.Navigator
import com.slack.circuitx.gesturenavigation.GestureNavigationDecoration
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivityCircuit : AppCompatActivity() {

    @Inject
    lateinit var circuit: Circuit

    @Inject
    lateinit var settingsDataStore: SettingsDataStore

    @Inject
    lateinit var userDataStore: UserDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUserLoginInfo(userDataStore)
        setContent {
            val backstack = rememberSaveableBackStack(root = MainUiScreen(title = "title"))
            val navigator = rememberCircuitNavigator(backstack)
            var selectedIndex by remember { mutableIntStateOf(0) }
            CircuitCompositionLocals(circuit = circuit) {
                RecipeTheme(settingsDataStore.isDark.value) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            NavBottomBar(
                                selectedIndex = selectedIndex,
                                onIndexChanged = { selectedIndex = it },
                                navigator = navigator
                            )
                        },
                        content = { paddingValues ->
                            Content(
                                paddingValues = PaddingValues(bottom = 0.dp),
                                navigator = navigator,
                                backstack = backstack
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun Content(
    paddingValues: PaddingValues,
    navigator: Navigator,
    backstack: SaveableBackStack
) {
    val gestureNavigationDecoration = remember(navigator) {
        GestureNavigationDecoration(onBackInvoked = navigator::pop)
    }
    ContentWithOverlays(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        NavigableCircuitContent(
            modifier = Modifier.background(Color.Transparent),
            navigator = navigator,
            backStack = backstack,
            decoration = gestureNavigationDecoration
        )
    }
}

private fun setUserLoginInfo(userDataStore: UserDataStore) {
    userDataStore.setAccessToken("Token 9c8b06d329136da358c2d00e76946b0111ce2c48")
}
