package com.me.recipe.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.me.recipe.ui.home.HomeScreen1
import com.me.recipe.ui.home.MainUiScreen
import com.me.recipe.ui.theme.RecipeTheme
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuitx.android.AndroidScreen
import com.slack.circuitx.android.AndroidScreenStarter
import com.slack.circuitx.android.rememberAndroidScreenAwareNavigator
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@AndroidEntryPoint
class MainActivityCircuit : AppCompatActivity() {

    @Inject
    lateinit var circuit: Circuit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val backstack = rememberSaveableBackStack(
                root = MainUiScreen(
                    title = "title",
                ),
            )
            val navigator =
                rememberAndroidScreenAwareNavigator(
                    delegate = rememberCircuitNavigator(backstack),
                    starter = remember { AndroidScreenStarter(::handleLegacyScreen) },
                )

            CircuitCompositionLocals(circuit = circuit) {
                RecipeTheme {
                    NavigableCircuitContent(navigator, backstack)
                }
            }
        }
    }

    private fun handleLegacyScreen(screen: AndroidScreen): Boolean {
        when (screen) {
            is HomeScreen1 -> {}
            else -> return false
        }
        return true
    }
}

@CircuitInject(MainUiScreen::class, SingletonComponent::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RememberReturnType")
@Composable
fun MainScreen(
    state: MainUiScreen,
    modifier: Modifier = Modifier,
) {
    Text("HI CIRCUIT ------------- HI ")
}
