package es.niadecode.numericabattleroyale.model.numerica

data class GameStateBo(
    var currentScore: Int,
    var maxScore: Int,
    var lastUserName: String,
    var lastUserNameMVP: String
)