package es.niadecode.numericabattleroyale.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import es.niadecode.numericabattleroyale.model.battle.SoldierData

@Composable
fun Soldier(soldierData: SoldierData) {

    Box(
        Modifier
            .offset(soldierData.point.x.dp, soldierData.point.y.dp)
            .size(soldierData.size.dp)
    ) {
        Image(
            painter = painterResource("vectors/robot_${soldierData.model}.svg"),
            null,
            colorFilter = ColorFilter.tint(soldierData.color)
        )
        Text(soldierData.soldierID, fontSize = 8.sp)
//        Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
//            drawPath(
//                color = soldierData.color,
//                path = Path().apply {
//                    val size = soldierData.size.dp.toPx()
//                    moveTo(0f, 0f) // Top-left corner...
//                    lineTo(size, 0f) // ...top right
//                    lineTo(size, size) // .. bottom right
//                    lineTo(0f, size) // ... to bottom-left corner.
//                    close()
//                }
//            )
//        })
    }
}
