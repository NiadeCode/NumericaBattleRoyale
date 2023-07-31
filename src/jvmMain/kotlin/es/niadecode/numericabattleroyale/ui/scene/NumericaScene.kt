package es.niadecode.numericabattleroyale.ui.scene

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import es.niadecode.numericabattleroyale.model.numerica.GameState
import es.niadecode.numericabattleroyale.ui.viewmodel.NumericaSceneViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import moe.tlaster.precompose.viewmodel.viewModel


@Composable
fun NumericaScene(navigatorCallback: (String) -> Unit) {
    val viewModel = viewModel(modelClass = NumericaSceneViewModel::class, keys = listOf(null)) {
        NumericaSceneViewModel()
    }

    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("HIGH SCORE ${state.maxScore}\n${state.lastUserNameMVP}")

        Text(state.currentScore.toString())


        if (state is GameState.GameOver) {
            val toBattleText = remember { mutableStateOf("") }
            LaunchedEffect(null) {
                var i = 3
                val counter = async {
                    while (i!=0) {
                        toBattleText.value = "Going to battle in $i"
                        delay(1000)
                        i--
                    }
                }
                counter.await()
                navigatorCallback("/battle")
            }
            Text(toBattleText.value)
        }


        Button({viewModel.toBattleMock()}) {
            Text("to battle mock")
        }



    }

}
