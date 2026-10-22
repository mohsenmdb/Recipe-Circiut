package com.me.recipe.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
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
import com.slack.circuit.foundation.rememberDefaultCircuitSaver
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
            val isDark = settingsDataStore.isDark.value
            DisposableEffect(isDark) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { isDark },
                    navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT) { isDark },
                )
                onDispose {}
            }
            RecipeTheme(isDark) {
                CompositionLocalProvider(LocalErrorFormatter provides errorFormatter) {
                    CircuitCompositionLocals(circuit = circuit) {
                        val backStack = rememberSaveableBackStack(
                            circuitSaver = rememberDefaultCircuitSaver(),
                            root = SplashScreen,
                        )
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
