package es.niadecode.numericabattleroyale.ui.scene

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import es.niadecode.numericabattleroyale.model.battle.BattleParticipation
import es.niadecode.numericabattleroyale.model.numerica.GameState
import es.niadecode.numericabattleroyale.ui.viewmodel.MainViewModel
import es.niadecode.numericabattleroyale.ui.viewmodel.NumericaSceneViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import moe.tlaster.precompose.viewmodel.viewModel


@Composable
fun NumericaScene(
    navigatorCallback: (String) -> Unit,
    navigateToBattle: (List<BattleParticipation>) -> Unit
) {
    val viewModel = viewModel(modelClass = NumericaSceneViewModel::class, keys = listOf(null)) {
        NumericaSceneViewModel()
    }


    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "HIGH SCORE ${state.maxScore}",
            style = MaterialTheme.typography.h1,
            color = MaterialTheme.colors.onBackground
        )
        Row {
            if (state.lastUserNameMVP.isNotEmpty()) {
                Text(
                    text = "by ",
                    style = MaterialTheme.typography.h1,
                    color = MaterialTheme.colors.onBackground
                )
                Text(
                    text = state.lastUserNameMVP,
                    style = MaterialTheme.typography.h1,
                    color = MaterialTheme.colors.primary
                )
            }
        }

        Card(
            modifier = Modifier
                .padding(30.dp)
                .size(300.dp),
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.h2,
                    text = state.currentScore.toString(),
                    color = MaterialTheme.colors.onSurface
                )
            }
        }

        Text(
            text = state.lastUserName,
            style = MaterialTheme.typography.h1,
            color = MaterialTheme.colors.onBackground,
        )

        Spacer(modifier = Modifier.size(24.dp))

        if (state is GameState.GameOver) {
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
                navigateToBattle(viewModel.battleParticipants)
            }
            Text(toBattleText.value, color = MaterialTheme.colors.onBackground)
        }

        Button(
            modifier = Modifier.alpha(0.5f),
            onClick = {
                viewModel.toBattleMock()
            }) {
            Text("to battle mock")
        }

    }

}
