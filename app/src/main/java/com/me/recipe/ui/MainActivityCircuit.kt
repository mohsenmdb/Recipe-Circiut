package com.me.recipe.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import com.me.recipe.shared.datastore.SettingsDataStore
import com.me.recipe.ui.component.util.LocalErrorFormatter
import com.me.recipe.ui.splash.SplashScreen
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.extention.getPushNotificationPermission
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuitx.android.rememberAndroidScreenAwareNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import recipe.app.core.errorformater.ErrorFormatter

@AndroidEntryPoint
class MainActivityCircuit : AppCompatActivity() {

    @Inject
    lateinit var circuit: Circuit

    @Inject
    lateinit var errorFormatter: ErrorFormatter

    @Inject
    lateinit var settingsDataStore: SettingsDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getPushNotificationPermission()
        setContent {
            RecipeTheme(settingsDataStore.isDark.value) {
                CompositionLocalProvider(LocalErrorFormatter provides errorFormatter) {
                    CircuitCompositionLocals(circuit = circuit) {
                        val backStack = rememberSaveableBackStack(root = SplashScreen)
                        val navigator = rememberAndroidScreenAwareNavigator(
                            delegate = rememberCircuitNavigator(
                                backStack = backStack,
                                onRootPop = { finish() },
                            ),
                            context = this,
                        )
                        NavigableCircuitContent(
                            navigator = navigator,
                            backStack = backStack,
                        )
                    }
                }
            }
        }
    }
}
