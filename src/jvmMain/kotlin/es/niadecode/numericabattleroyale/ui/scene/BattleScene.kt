package es.niadecode.numericabattleroyale.ui.scene

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import es.niadecode.numericabattleroyale.model.battle.BattleParticipation
import es.niadecode.numericabattleroyale.model.battle.getMockParticipationList
import es.niadecode.numericabattleroyale.ui.composable.Soldier
import es.niadecode.numericabattleroyale.ui.state.BattleState
import es.niadecode.numericabattleroyale.ui.viewmodel.BattleSceneViewModel
import kotlinx.coroutines.async
import java.awt.Point
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay
import moe.tlaster.precompose.viewmodel.viewModel

@Composable
fun BattleScene(
    participations: List<BattleParticipation>,
    navigatorCallback: (String) -> Unit
) {

    val viewModel = viewModel(modelClass = BattleSceneViewModel::class, keys = listOf(null)) {
        BattleSceneViewModel()
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {

        val state = viewModel.state.collectAsState()

        if (state.value == BattleState.START) {
            val toBattleText = remember { mutableStateOf("") }
            LaunchedEffect(null) {
                var i = 3
                val counter = async {
                    while (i != 0) {
                        toBattleText.value = "Going to battle in $i"
                        delay(1000)
                        i--
                    }
                }
                counter.await()
                //navigatorCallback("/battle")
            //    navigateToBattle(viewModel.battleParticipants)
            }
            Text(toBattleText.value, color = MaterialTheme.colors.onBackground)
        }

//    val state by viewModel.state.collectAsState()

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
