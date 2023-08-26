package es.niadecode.numericabattleroyale.ui.scene

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import es.niadecode.numericabattleroyale.model.battle.BattleParticipation
import es.niadecode.numericabattleroyale.model.numerica.GameState
import es.niadecode.numericabattleroyale.repository.SettingsRepository
import es.niadecode.numericabattleroyale.ui.viewmodel.NumericaSceneViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import moe.tlaster.precompose.viewmodel.viewModel


@Composable
fun NumericaScene(
    settingsRepository: SettingsRepository,
    goBack: () -> Unit,
    navigateToBattle: (List<BattleParticipation>) -> Unit
) {
    val viewModel = viewModel(modelClass = NumericaSceneViewModel::class, keys = listOf(null)) {
        NumericaSceneViewModel(settingsRepository)
    }


    val state by viewModel.state.collectAsState()

    IconButton(
        modifier = Modifier.size(30.dp),
        onClick = {
            goBack()
        }) {
        Icon(
            painter = painterResource("settings_FILL1_wght400_GRAD0_opsz48.svg"),
            null,
            tint = MaterialTheme.colors.primary
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            if (state.gloryUserName.isNotEmpty()) {
                Text(
                    text = "GLORIA A ",
                    style = MaterialTheme.typography.h1,
                    color = MaterialTheme.colors.onBackground
                )
                Text(
                    text = state.gloryUserName,
                    style = MaterialTheme.typography.h1,
                    color = MaterialTheme.colors.primary
                )
            } else {
                Text(
                    text = "Vence en la batalla para llevarte la gloria",
                    style = MaterialTheme.typography.h1,
                    color = MaterialTheme.colors.onBackground
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

        Spacer(modifier = Modifier.size(5.dp))

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
                navigateToBattle(viewModel.battleParticipants)
            }
            Text(toBattleText.value, color = MaterialTheme.colors.onBackground, style = MaterialTheme.typography.h1)
        }

    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Button(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .alpha(0.2f),
            onClick = {
                viewModel.toBattleMock()
            }) {
            Text("to battle mock")
        }

        Column(
            modifier = Modifier.align(Alignment.BottomEnd)
                .background(MaterialTheme.colors.secondary.copy(alpha = .2f))
        ) {
            viewModel.battleParticipants.forEach {
                Text(
                    text = "${it.name} ${it.soldiers}",
                    style = MaterialTheme.typography.body1,
                    color = it.color
                )
            }
        }

    }

}
