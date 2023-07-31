import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import es.niadecode.numericabattleroyale.ui.App
import moe.tlaster.precompose.PreComposeWindow

fun main() {
    application {

        val windowState = rememberWindowState()
        windowState.size = DpSize(512.dp, 512.dp)

        PreComposeWindow(
            state = windowState,
            onCloseRequest = ::exitApplication
        ) {
            App()
        }
    }
}
