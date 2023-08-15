package es.niadecode.numericabattleroyale.model.battle

import androidx.compose.ui.graphics.Color

data class BattleParticipation(val name: String, val color: Color, var soldiers: Int)


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

val BattleColors = listOf(
    Color(0xFF0048BA),
    Color(0xFFB0BF1A),
    Color(0xFFB284BE),
    Color(0xFF72A0C1),
    Color(0xFFF0F8FF),
    Color(0xFFDB2D43),
    Color(0xFFC46210),
    Color(0xFFEFDECD),
    Color(0xFF9F2B68),
    Color(0xFFF19CBB),
    Color(0xFFAB274F),
    Color(0xFF3B7A57),
    Color(0xFFFFBF00),
    Color(0xFF9966CC),
    Color(0xFF3DDC84),
    Color(0xFFCD9575),
    Color(0xFF665D1E),
    Color(0xFF915C83),
    Color(0xFF841B2D),
    Color(0xFFFAEBD7),
    Color(0xFFFBCEB1),


)