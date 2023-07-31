package es.niadecode.numericabattleroyale.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import es.niadecode.numericabattleroyale.ui.scene.BattleScene
import es.niadecode.numericabattleroyale.ui.scene.HomeScene
import es.niadecode.numericabattleroyale.ui.scene.NumericaScene
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
    MaterialTheme {
        NavHost(
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
}
