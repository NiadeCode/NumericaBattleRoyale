package es.niadecode.numericabattleroyale.ui.scene

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import es.niadecode.numericabattleroyale.ui.viewmodel.HomeSceneViewModel
import moe.tlaster.precompose.viewmodel.viewModel

@Composable
fun HomeScene(navigatorCallback: (String) -> Unit) {

    val viewModel = viewModel(modelClass = HomeSceneViewModel::class, keys = listOf(null)) {
        HomeSceneViewModel()
    }

    val state by viewModel.state.collectAsState()
    val navigate by viewModel.navigate.collectAsState()

    if (navigate){
        navigatorCallback("/numerica")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
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
            Checkbox(
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


        Button(onClick = {
//            navigatorCallback("/numerica")
            viewModel.connectTwitch()
        }) {
            Text("Conectar a twitch", style = MaterialTheme.typography.button)
        }
    }
}
