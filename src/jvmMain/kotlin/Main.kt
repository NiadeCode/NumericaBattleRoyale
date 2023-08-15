import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import es.niadecode.numericabattleroyale.ui.App
import es.niadecode.numericabattleroyale.ui.theme.BattleRoyaleTheme
import moe.tlaster.precompose.PreComposeWindow

fun main() {
    application {

        val windowState = rememberWindowState()
        windowState.size = DpSize(512.dp, 542.dp)
        BattleRoyaleTheme {
            PreComposeWindow(
                resizable = false, undecorated = true, state = windowState, onCloseRequest = ::exitApplication
            ) {
                Column(
                    modifier = Modifier.background(MaterialTheme.colors.background)
                ) {
                    DraggableBar(this@PreComposeWindow, this@application)
                    App()
                }
            }
        }
    }
}

@Composable
private fun DraggableBar(
    frameWindowScope: FrameWindowScope, applicationScope: ApplicationScope
) {
    Row(
        modifier = Modifier.background(color = MaterialTheme.colors.primaryVariant)
            .fillMaxWidth()
            .height(30.dp)
            .padding(start = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        frameWindowScope.WindowDraggableArea(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Numerica Battle Royale",
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.onPrimary
            )
        }
        Row {
            IconButton(onClick = {
                applicationScope.exitApplication()
            }) {
                Icon(
                    painterResource("close_FILL0_wght400_GRAD0_opsz48.svg"),
                    null,
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}




