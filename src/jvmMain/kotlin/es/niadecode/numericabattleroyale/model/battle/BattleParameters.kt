package es.niadecode.numericabattleroyale.model.battle

import androidx.compose.ui.graphics.Color

data class BattleParticipation(
    val name: String,
    val userId: String,
    val isMod: Boolean,
    val color: Color,
    var soldiers: Int
)


fun getMockParticipationList(): List<BattleParticipation> {
    return listOf(
        BattleParticipation("NiadeCode", "null", true, BattleColors[0], 1 + 15 + 17),
        BattleParticipation("RothioTome", "null", false, BattleColors[1], 14),
        BattleParticipation("Elendow", "null", false, BattleColors[2], 2 + 9),
        BattleParticipation("Niv3k_El_Pato", "null", false, BattleColors[3], 3 + 6 + 16),
        BattleParticipation("jynus", "null", false, BattleColors[4], 4 + 10),
        BattleParticipation("Montxaldre", "null", false, BattleColors[5], 5 + 13),
        BattleParticipation("PSuzume", "null", false, BattleColors[6], 7 + 12),
        BattleParticipation("Ildesir", "null", false, BattleColors[7], 8 + 11),
        BattleParticipation("AlgebroDev", "null", false, BattleColors[8], 18),
        BattleParticipation("Demiterra", "null", false, BattleColors[9], 19),
        BattleParticipation("CapitanYelmo", "null", false, BattleColors[10], 20),
        BattleParticipation("mecadiego112", "null", false, BattleColors[11], 21),
        BattleParticipation("neusR", "null", false, BattleColors[12], 22),
        BattleParticipation("pello", "null", false, BattleColors[13], 23),
        BattleParticipation("pepeCoral", "null", false, BattleColors[14], 24),
        BattleParticipation("RoyNewo", "null", false, BattleColors[15], 25),
        BattleParticipation("NekeRafa", "null", false, BattleColors[16], 26),
        BattleParticipation("ManzDev", "null", false, BattleColors[17], 27),
        BattleParticipation("afor_digital", "null", false, BattleColors[18], 28),
        BattleParticipation("bruno", "null", false, BattleColors[19], 28),
        BattleParticipation("seyacat", "null", false, BattleColors[20], 28),
        BattleParticipation("cakuDev", "null", false, BattleColors[21], 28),
        BattleParticipation("MysteriousHat", "null", false, BattleColors[22], 28),
        BattleParticipation("joeMancera", "null", false, BattleColors[23], 28),




    )
}

val BattleColors = listOf(
    Color(0xFF0048BA),
    Color(0xFFB0BF1A),
    Color(0xFFB284BE),
    Color(0xFF72A0C1),
//    Color(0xFFF0F8FF),
    Color(0xFFDB2D43),
    Color(0xFFC46210),
//    Color(0xFFEFDECD),
    Color(0xFF9F2B68),
    Color(0xFFF19CBB),
//    Color(0xFFAB274F),
    Color(0xFF3B7A57),
    Color(0xFFFFBF00),
    Color(0xFF9966CC),
    Color(0xFF3DDC84),
    Color(0xFFCD9575),
    Color(0xFF665D1E),
    Color(0xFF915C83),
    Color(0xFF841B2D),
//    Color(0xFFFAEBD7),
    Color(0xFFFBCEB1),
    Color(0xFF00FFFF),
    Color(0xFF58427C),
    Color(0xFFFFD300),
    Color(0xFFF56FA1),
    Color(0xFF654321),
    Color(0xFF5D3954),
    Color(0xFF008B8B),
    Color(0xFF006400),
)