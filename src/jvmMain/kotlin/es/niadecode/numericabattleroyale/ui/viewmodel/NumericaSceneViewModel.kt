package es.niadecode.numericabattleroyale.ui.viewmodel

import es.niadecode.numericabattleroyale.model.battle.BattleColors
import es.niadecode.numericabattleroyale.model.battle.BattleParticipation
import es.niadecode.numericabattleroyale.model.battle.getMockParticipationList
import es.niadecode.numericabattleroyale.model.numerica.GameParticipation
import es.niadecode.numericabattleroyale.model.numerica.GameState
import es.niadecode.numericabattleroyale.model.numerica.mapToBo
import es.niadecode.numericabattleroyale.model.numerica.mapToVo
import es.niadecode.numericabattleroyale.repository.SettingsRepository
import es.niadecode.numericabattleroyale.repository.TwitchChatRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class NumericaSceneViewModel(val settingsRepository: SettingsRepository) : ViewModel() {

    private val chatRepository by lazy { TwitchChatRepository(viewModelScope, settingsRepository) }

    private val _state by lazy {
        val user = runBlocking { settingsRepository.getVipNameUser() }
        MutableStateFlow<GameState>(GameState.Start(user))
    }
    val state: StateFlow<GameState> = _state

    private val _participantsState by lazy {
        MutableStateFlow<List<BattleParticipation>>(listOf())
    }
    val participantsState: StateFlow<List<BattleParticipation>> = _participantsState

//    val battleParticipants = mutableListOf<BattleParticipation>()

    override fun onCleared() {
        chatRepository.close()
        super.onCleared()
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("Handle $exception in CoroutineExceptionHandler")
    }

    init {

        println("init viewmodel")
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            chatRepository.participationFlow
                .collect {
                    onParticipationReceived(it)
                }
        }
    }


    private suspend fun onParticipationReceived(gameParticipation: GameParticipation) {

        val current = state.value.mapToBo()

        if (gameParticipation.userName == current.lastUserName) {
//            return //TODO uncomment to prevent participation from the same player
        }

        if (gameParticipation.isTroll) {
            addTroll(gameParticipation)
            return
        }

        if (gameParticipation.number == current.currentScore + 1) {


            addBattleParticipation(gameParticipation)

            current.currentScore++
            current.lastUserName = gameParticipation.userName

            if (gameParticipation.number >= settingsRepository.getmaxParticipation()) {
                _state.value = GameState.GameOver(
                    current.currentScore,
                    current.maxScore,
                    current.lastUserName,
                    current.lastUserNameMVP
                )
                return
            }

            _state.value = current.mapToVo()
        } else if (
            settingsRepository.getBattleOnFail()
            && settingsRepository.getTrolls()
            && gameParticipation.number
            !in current.currentScore - 1..current.currentScore + 2
        ) {
            addTroll(gameParticipation.copy(number = 1))
        } else {
            if (settingsRepository.getBattleOnFail()
                && current.currentScore != 0
                && participantsState.value.size > 1
            ) {
                _state.value = GameState.GameOver(
                    current.currentScore,
                    current.maxScore,
                    current.lastUserName,
                    current.lastUserNameMVP
                )
            }
        }
    }

    private suspend fun addBattleParticipation(gameParticipation: GameParticipation) {

        if (participantsState.value.find { it.name == gameParticipation.userName } != null) {

            val newList = participantsState.value.map {
                if (it.name == gameParticipation.userName) {
                    it.copy(soldiers = it.soldiers + gameParticipation.number)
                } else {
                    it
                }
            }
            _participantsState.emit(newList)
        } else {

            val newList = participantsState.value.toMutableList()
            newList.add(
                BattleParticipation(
                    gameParticipation.userName,
                    gameParticipation.userId,
                    gameParticipation.mod,
                    BattleColors[participantsState.value.size % BattleColors.size],
                    gameParticipation.number
                )
            )

            _participantsState.emit(newList)
        }
    }

    private suspend fun addTroll(gameParticipation: GameParticipation) {

        if (participantsState.value.find { it.name == gameParticipation.userName } != null) {

            val newList = participantsState.value.map {
                if (it.name == gameParticipation.userName) {
                    it.copy(soldiers = gameParticipation.number)
                } else {
                    it
                }
            }
            _participantsState.emit(newList)
        } else {

            val newList = participantsState.value.toMutableList()
            newList.add(
                BattleParticipation(
                    gameParticipation.userName,
                    gameParticipation.userId,
                    gameParticipation.mod,
                    BattleColors[participantsState.value.size % BattleColors.size],
                    gameParticipation.number
                )
            )

            _participantsState.emit(newList)
        }
    }

    fun toBattleMock() {
        viewModelScope.launch {
            val bo = _state.value.mapToBo()
            _state.value =
                GameState.GameOver(bo.currentScore, bo.maxScore, bo.lastUserName, settingsRepository.getVipNameUser())
            _participantsState.emit(
                getMockParticipationList()
            )
        }
    }


    /*
    original code by @RothioTome
    private void OnTwitchMessageReceived(string username, string message)
     {
         if(int.TryParse(message, out int response))
         {
             if (lastUsername.Equals(username)) return;

             if (response == currentScore + 1)
             {
                 currentScore++;
                 usernameTMP.SetText(username);
                 currentScoreTMP.SetText(currentScore.ToString());
                 lastUsername = username;
                 if (currentScore > currentMaxScore)
                 {
                     currentMaxScore = currentScore;
                     maxScoreTMP.SetText($"HIGH SCORE: {currentMaxScore}");
                     PlayerPrefs.SetInt(maxScoreKey, currentScore);
                 }
             }
             else
             {
                 if (currentScore != 0)
                 {
                     usernameTMP.SetText($"<color=#00EAC0>Shame on </color>{username}<color=#00EAC0>!</color>");
                     GameLost();
                 }

             }
         }
     }*/

}

