package es.niadecode.numericabattleroyale.ui.scene

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import es.niadecode.numericabattleroyale.model.battle.BattleParticipation
import es.niadecode.numericabattleroyale.repository.SettingsRepository
import es.niadecode.numericabattleroyale.ui.composable.Soldier
import es.niadecode.numericabattleroyale.ui.state.BattleState
import es.niadecode.numericabattleroyale.ui.viewmodel.BattleSceneViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import moe.tlaster.precompose.viewmodel.viewModel
import java.awt.Point
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun BattleScene(
    settingsRepository: SettingsRepository,
    participations: List<BattleParticipation>,
    backToNumerica: () -> Unit
) {

    val viewModel = viewModel(modelClass = BattleSceneViewModel::class, keys = listOf(null)) {
        BattleSceneViewModel(settingsRepository)
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {

        val state = viewModel.state.collectAsState()

        if (state.value == BattleState.GLORY) {
            val toBattleText = remember { mutableStateOf("") }
            LaunchedEffect(null) {
                var i = 3
                val counter = async {
                    while (i != 0) {
                        toBattleText.value = "Back to Numerica in $i"
                        delay(1000)
                        i--
                    }
                }
                counter.await()
                backToNumerica()
            }
            Text(
                modifier = Modifier.align(Alignment.BottomCenter),
                text = toBattleText.value,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.body1
            )
        }


        if (state.value == BattleState.START) {
            viewModel.startGame(participations, Point(256, 256), 180.0)
        }

        LaunchedEffect(Unit) {
            while (state.value == BattleState.PLAYING) {
                viewModel.update()
                delay(30.milliseconds)
            }
        }

        val soldiers by viewModel.soldiers.collectAsState()

        Column(
            modifier = Modifier.fillMaxSize()
                .alpha(0.3f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = viewModel.gameStatus,
                style = MaterialTheme.typography.h1,
                color = MaterialTheme.colors.onBackground
            )
        }

        soldiers.forEach {
            Soldier(it)
        }

        Column(
            modifier = Modifier.align(Alignment.BottomEnd)
                .background(MaterialTheme.colors.secondary.copy(alpha = .2f))
        ) {
            viewModel.participantsWidget.forEach {
                Text(
                    text = "${it.userName} ${it.soldiers}",
                    style = MaterialTheme.typography.body1,
                    color = it.color
                )
            }
        }

    }
}
