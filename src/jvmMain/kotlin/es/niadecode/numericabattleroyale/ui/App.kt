package es.niadecode.numericabattleroyale.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.window.WindowScope
import es.niadecode.numericabattleroyale.ui.scene.BattleScene
import es.niadecode.numericabattleroyale.ui.scene.HomeScene
import es.niadecode.numericabattleroyale.ui.scene.NumericaScene
import es.niadecode.numericabattleroyale.ui.theme.BattleRoyaleTheme
import es.niadecode.numericabattleroyale.ui.viewmodel.MainViewModel
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.viewmodel.viewModel

@Composable
fun App() {

    val viewModel = viewModel(modelClass = MainViewModel::class, keys = listOf(null)) {
        MainViewModel()
    }

    val navigator = rememberNavigator()

        NavHost(
            modifier = Modifier.background(MaterialTheme.colors.background),
            navigator = navigator,
            initialRoute = "/home"
        ) {

            scene("/home") {
                HomeScene { navigator.navigate(it) }
            }

            scene("/numerica") {
                NumericaScene { navigator.navigate(it) }
            }

            scene("/battle") {
                BattleScene { navigator.navigate("/numerica") }
            }
    }
}

