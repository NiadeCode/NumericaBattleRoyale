package es.niadecode.numericabattleroyale.ui.scene

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import es.niadecode.numericabattleroyale.repository.SettingsRepository
import es.niadecode.numericabattleroyale.ui.viewmodel.HomeSceneViewModel
import moe.tlaster.precompose.viewmodel.viewModel

@Composable
fun HomeScene(
    settingsRepository: SettingsRepository,
    navigatorCallback: (String) -> Unit
) {

    val viewModel = viewModel(modelClass = HomeSceneViewModel::class, keys = listOf(null)) {
        HomeSceneViewModel(settingsRepository)
    }

    val state by viewModel.state.collectAsState()
    val navigate by viewModel.navigate.collectAsState()

    if (navigate) {
        navigatorCallback("/numerica")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(50.dp))
            Checkbox(
                enabled = state.isConnected.not(),
                modifier = Modifier.width(100.dp),
                checked = state.timeout,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colors.primary,
                    uncheckedColor = MaterialTheme.colors.primary,
                    checkmarkColor = MaterialTheme.colors.onPrimary,
                ),

                onCheckedChange = {
                    viewModel.setTimeouts(it)
                })

            Text(
                "Hacer timeouts",
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.onBackground
            )

        }


        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(50.dp))
            Checkbox(
                enabled = state.isConnected.not(),
                modifier = Modifier.width(100.dp),
                checked = state.vip,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colors.primary,
                    uncheckedColor = MaterialTheme.colors.primary,
                    checkmarkColor = MaterialTheme.colors.onPrimary,
                ),

                onCheckedChange = {
                    viewModel.setVip(it)
                })

            Text(
                "Hacer vip al vencedor",
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.onBackground
            )

        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(50.dp))

            TextField(
                enabled = state.timeout,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(100.dp),
                value = state.timeoutMultiplier.toString(),
                onValueChange = {
                    viewModel.updateTimeoutMultiplier(it.toIntOrNull())
                }
            )
            Text(
                "Multiplicador de timeouts",
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.onBackground
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(50.dp))

            Checkbox(
                enabled = state.timeout,
                modifier = Modifier.width(100.dp),
                checked = state.modImmunity,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colors.primary,
                    uncheckedColor = MaterialTheme.colors.primary,
                    checkmarkColor = MaterialTheme.colors.onPrimary,
                ),

                onCheckedChange = {
                    viewModel.setModImmunity(it)
                })

            Text(
                "Mods inmunes",
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.onBackground
            )

        }


        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(50.dp))

            TextField(
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(100.dp),
                value = state.maxParticipations.toString(),
                onValueChange = {
                    viewModel.updateMax(it.toIntOrNull())
                }
            )
            Text(
                "numero maximo (puede influir en el rendimiento de la batalla)",
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.onBackground
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(50.dp))
            Checkbox(
                modifier = Modifier.width(100.dp),
                checked = state.batleOnFail,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colors.primary,
                    uncheckedColor = MaterialTheme.colors.primary,
                    checkmarkColor = MaterialTheme.colors.onPrimary,
                ),

                onCheckedChange = {
                    viewModel.setBattleOnFail(it)
                })

            Text(
                "Iniciar batalla al fallar un número",
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.onBackground
            )

        }




        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
//            navigatorCallback("/numerica")
                viewModel.connectTwitch()
            }) {
            Text(
                if (state.isConnected.not()) "Conectar a twitch" else {
                    "volver al juego"
                }, style = MaterialTheme.typography.button
            )
        }
    }
}
