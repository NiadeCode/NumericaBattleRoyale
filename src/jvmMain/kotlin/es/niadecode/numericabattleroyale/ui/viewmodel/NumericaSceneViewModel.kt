package es.niadecode.numericabattleroyale.ui.viewmodel

import es.niadecode.numericabattleroyale.model.numerica.GameParticipation
import es.niadecode.numericabattleroyale.model.numerica.GameState
import es.niadecode.numericabattleroyale.model.numerica.mapToBo
import es.niadecode.numericabattleroyale.model.numerica.mapToVo
import es.niadecode.numericabattleroyale.repository.TwitchChatRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class NumericaSceneViewModel : ViewModel() {

    private val chatRepository by lazy { TwitchChatRepository(viewModelScope) }

    private val _state by lazy {
        MutableStateFlow<GameState>(GameState.Start)
    }
    val state: StateFlow<GameState> = _state

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


    private fun onParticipationReceived(gameParticipation: GameParticipation) {

        val current = state.value.mapToBo()

        if (gameParticipation.userName == current.lastUserName) {
            //        return //TODO uncomment to prevent participation from the same player
        }

        if (gameParticipation.number == current.currentScore + 1) {
            current.currentScore++
            current.lastUserName = gameParticipation.userName

            if (current.currentScore > current.maxScore) {
                current.maxScore = current.currentScore
                current.lastUserNameMVP = gameParticipation.userName
            }

            _state.value = current.mapToVo()
        } else {
            if (current.currentScore != 0) {
                _state.value = GameState.GameOver(current.maxScore, current.lastUserName, current.lastUserNameMVP)
            }
        }
    }

    fun toBattleMock() {
        _state.value = GameState.GameOver(5, "NiadeCode", "NiadeCode")
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

