package es.niadecode.numericabattleroyale.model.battle

import androidx.compose.ui.graphics.Color

data class BattleParticipation(val name: String, val color: Color, val soldiers: Int)


fun getMockParticipationList(): List<BattleParticipation> {
    return listOf(
        BattleParticipation("NiadeCode", Color.Blue, 1),
        BattleParticipation("RothioTome", Color(0xFFFF5850), 9),
        BattleParticipation("RothioTome2", Color.Blue, 2),
        BattleParticipation("RothioTome3", Color.Black, 3 + 6),
        BattleParticipation("RothioTome4", Color.Cyan, 4),
        BattleParticipation("RothioTome5", Color.Green, 5),
        BattleParticipation("RothioTome6", Color.Yellow, 7),
        BattleParticipation("RothioTome7", Color.Red, 8),
    )
}