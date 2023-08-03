package es.niadecode.numericabattleroyale.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable


private val LightColorPalette = lightColors(
    primary = Raspberry,
    primaryVariant = Amaranth,
    secondary = Aquamarine,
    surface = Aquamarine,
    background = Claret,
    onPrimary = Mustard,
    onSecondary = Aquamarine,
    onSurface = Aquamarine,


    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun BattleRoyaleTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    //TODO accessibility config
    val colors =
//        if (darkTheme) {
//        DarkColorPalette
//    } else {
        LightColorPalette
//    }


    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}