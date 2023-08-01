package es.niadecode.numericabattleroyale.model.battle

import androidx.compose.ui.graphics.Color

data class BattleParticipation(val name: String, val color: Color, val soldiers: Int)


fun getMockParticipationList(): List<BattleParticipation> {
    return listOf(
        BattleParticipation("NiadeCode", Color.Blue, 5),
        BattleParticipation("RothioTome", Color(0xFFFF5850), 10),
        BattleParticipation("RothioTome2", Color.Blue, 10),
        BattleParticipation("RothioTome3", Color.Black, 10),
        BattleParticipation("RothioTome4", Color.Cyan, 10),
        BattleParticipation("RothioTome5", Color.Green, 10),
        BattleParticipation("RothioTome6", Color.Yellow, 10),
       BattleParticipation("RothioTome7", Color.Red, 10),
    )
}