package es.niadecode.numericabattleroyale.ui.scene

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import es.niadecode.numericabattleroyale.model.battle.BattleParticipation
import es.niadecode.numericabattleroyale.model.battle.getMockParticipationList
import es.niadecode.numericabattleroyale.ui.composable.Soldier
import es.niadecode.numericabattleroyale.ui.state.BattleState
import es.niadecode.numericabattleroyale.ui.viewmodel.BattleSceneViewModel
import java.awt.Point
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay
import moe.tlaster.precompose.viewmodel.viewModel

@Composable
fun BattleScene(
    participations: List<BattleParticipation> = getMockParticipationList(),
    navigatorCallback: (String) -> Unit
) {

    val viewModel = viewModel(modelClass = BattleSceneViewModel::class, keys = listOf(null)) {
        BattleSceneViewModel()
    }

    var with: Int = 0
    var height: Int = 0
    val density = LocalDensity.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged {
                with(density) {
                    with = it.width / 2
                    height = it.height / 2
                }
            },
    ) {
//    val state by viewModel.state.collectAsState()

        if (viewModel.gameState == BattleState.START) {
            viewModel.startGame(participations, Point(256, 256), 80.0)
        }

        LaunchedEffect(Unit) {
            while (true) {
                    viewModel.update()
                    delay(16.milliseconds)
            }
        }

        val soldiers by viewModel.soldiers.collectAsState()

        Column(
            modifier = Modifier.fillMaxSize()
                .alpha(0.3f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Button(onClick = { navigatorCallback("/numerica") }) {
                Text("Volver")
            }

            Text("Batalla WIP")


        }

        soldiers.forEach {
            Soldier(it)
        }

    }
}
