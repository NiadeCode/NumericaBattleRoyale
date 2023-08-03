package es.niadecode.numericabattleroyale.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.sp

val piedra = FontFamily(
    Font(
        resource = "fonts/Piedra-Regular.ttf",
        weight = FontWeight.W600,
        style = FontStyle.Normal,
    )
)

// Set of Material typography styles to start with
val Typography = Typography(
    h1 = TextStyle(
        fontFamily = piedra,
        fontWeight = FontWeight.Black,
        fontSize = 24.sp,
        letterSpacing = 1.sp,
    ),
    h2 = TextStyle(
        fontFamily = piedra,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
    ),
    body1 = TextStyle(
        fontFamily = piedra,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
    ),

    body2 = TextStyle(
        fontFamily = piedra,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
    ),

    button = TextStyle(
        fontFamily = piedra,
        fontSize = 20.sp,
        letterSpacing = 1.sp
    ),
    defaultFontFamily = piedra
)
