package es.niadecode.numericabattleroyale.ui.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import es.niadecode.numericabattleroyale.model.battle.SoldierData

@Composable
fun Soldier(soldierData: SoldierData) {

    Box(
        Modifier
            .offset(soldierData.point.x.dp, soldierData.point.y.dp)
            .size(soldierData.size.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
            drawPath(
                color = soldierData.color,
                path = Path().apply {
                    val size = soldierData.size.dp.toPx()
                    moveTo(0f, 0f) // Top-left corner...
                    lineTo(size, 0f) // ...top right
                    lineTo(size, size) // .. bottom right
                    lineTo(0f, size) // ... to bottom-left corner.
                    close()
                }
            )
        })
    }
}
