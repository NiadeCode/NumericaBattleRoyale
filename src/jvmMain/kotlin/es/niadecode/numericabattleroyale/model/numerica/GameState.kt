package es.niadecode.numericabattleroyale.model.numerica

sealed class GameState(
    open val currentScore: Int,
    open val maxScore: Int,
    open val lastUserName: String,
    open val gloryUserName: String,
) {

    data class Start(
        override val gloryUserName: String
    ) : GameState(0, 0, "", "")
    data class Play(
        override val currentScore: Int,
        override val maxScore: Int,
        override val lastUserName: String,
        override val gloryUserName: String
    ) : GameState(currentScore, maxScore, lastUserName, gloryUserName)

    data class GameOver(
        override val currentScore: Int,
        override val maxScore: Int,
        override val lastUserName: String,
        override val gloryUserName: String,
    ) : GameState(currentScore, maxScore, lastUserName, gloryUserName)

}

fun GameState.mapToBo(): GameStateBo {
    return when (this) {
        is GameState.GameOver -> {
            GameStateBo(0, maxScore, lastUserName, gloryUserName)
        }

        is GameState.Play -> {
            GameStateBo(currentScore, maxScore, lastUserName, gloryUserName)
        }

        is GameState.Start -> {
            GameStateBo(0, 0, "", gloryUserName)
        }
    }
}

fun GameStateBo.mapToVo(): GameState {
    return GameState.Play(currentScore, maxScore, lastUserName, lastUserNameMVP)
}