package com.me.recipe.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import com.me.recipe.shared.datastore.SettingsDataStore
import com.me.recipe.shared.datastore.UserDataStore
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
        getPushNotificationPermission()
        setContent {
            LaunchedEffect(Unit) {
                setUserLoginInfo(userDataStore)
            }
            CircuitCompositionLocals(circuit = circuit) {
                RecipeTheme(settingsDataStore.isDark.value) {
                    val backStack = rememberSaveableBackStack(root = SplashScreen)
                    val navigator = rememberAndroidScreenAwareNavigator(
                        delegate = rememberCircuitNavigator(backStack, onRootPop = { finish() }),
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

private suspend fun setUserLoginInfo(userDataStore: UserDataStore) {
    userDataStore.setAccessToken("Token 9c8b06d329136da358c2d00e76946b0111ce2c48")
}
