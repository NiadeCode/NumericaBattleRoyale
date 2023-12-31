package es.niadecode.numericabattleroyale.ui

import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import es.niadecode.numericabattleroyale.model.battle.BattleParticipation
import es.niadecode.numericabattleroyale.repository.SettingsRepository
import es.niadecode.numericabattleroyale.ui.scene.BattleScene
import es.niadecode.numericabattleroyale.ui.scene.HomeScene
import es.niadecode.numericabattleroyale.ui.scene.NumericaScene
import es.niadecode.numericabattleroyale.ui.viewmodel.MainViewModel
import es.niadecode.numericabattleroyale.util.createPreferences
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.viewmodel.viewModel

@Composable
fun App() {

    val viewModel = viewModel(modelClass = MainViewModel::class, keys = listOf(null)) {
        MainViewModel()
    }
    val settingsRepository = SettingsRepository(createPreferences())

    var battleParticipations = remember { emptyList<BattleParticipation>() }

    val navigator = rememberNavigator()

    NavHost(
        modifier = Modifier.background(MaterialTheme.colors.background),
        navigator = navigator,
        initialRoute = "/home",
    ) {

        scene("/home") {
            HomeScene(
                settingsRepository
            ) { navigator.navigate(it, options = NavOptions(launchSingleTop = true)) }
        }

        scene("/numerica") {
            NumericaScene(
                settingsRepository,
                goBack = {
                    navigator.navigate("/home", options = NavOptions(launchSingleTop = true))
                }
            ) {
                battleParticipations = it
                navigator.navigate("/battle")
            }
        }

        scene("/battle") {
            BattleScene(
                settingsRepository,
                battleParticipations
            ) {
                navigator.navigate("/numerica")
            }
        }
    }
}

