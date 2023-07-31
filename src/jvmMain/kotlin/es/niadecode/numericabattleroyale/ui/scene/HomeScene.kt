package es.niadecode.numericabattleroyale.ui.scene

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HomeScene(navigatorCallback: (String) -> Unit) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Button(onClick = { navigatorCallback("/numerica") }) {
            Text("Jugar")
        }
    }
}
