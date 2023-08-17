package es.niadecode.numericabattleroyale.ui.viewmodel

import es.niadecode.numericabattleroyale.model.config.ConfigState
import es.niadecode.numericabattleroyale.repository.SettingsRepository
import es.niadecode.numericabattleroyale.repository.TwitchApiRepository
import es.niadecode.numericabattleroyale.repository.TwitchLoginRepository
import es.niadecode.numericabattleroyale.repository.twitchAuthUrl
import es.niadecode.numericabattleroyale.util.createPreferences
import es.niadecode.numericabattleroyale.util.openWebpage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import java.util.*

class HomeSceneViewModel(
) : ViewModel() {

    private val settingsRepository: SettingsRepository by lazy { SettingsRepository(createPreferences()) }
    private val apiRepository by lazy { TwitchApiRepository(viewModelScope, settingsRepository) }
    private val loginRepository by lazy { TwitchLoginRepository(viewModelScope, settingsRepository) }

    private val _state by lazy {
        MutableStateFlow(
            ConfigState(
                timeout = false,
                timeoutMultiplier = 10,
                vip = false,
                modImmunity = false,
                isConnected = false
            )
        )
    }
    val state: StateFlow<ConfigState> = _state

    private val _navigate = MutableStateFlow(false)
    val navigate: StateFlow<Boolean> = _navigate

    override fun onCleared() {
        super.onCleared()
        loginRepository.stopServer()
//        stopServer()
    }

    fun setTimeouts(newTimeoutsBoolean: Boolean) {
        _state.value = state.value.copy(timeout = newTimeoutsBoolean)
    }

    fun setTimeoutsMultiplier(newMultiplier: Int) {
        _state.value = state.value.copy(timeoutMultiplier = newMultiplier)
    }

    fun setModImmunity(newModImmunity: Boolean) {
        _state.value = state.value.copy(modImmunity = newModImmunity)
    }

    fun connectTwitch() {
        val port = 38769
        val s = createAutorizationUrl(port)
        loginRepository.startServer(port)

        viewModelScope.launch {
            loginRepository.tokenFlow.collect {
                apiRepository.validate().collect {
                    _navigate.value = true
                    delay(50)
                    _navigate.value = false
                }
            }
        }

        openWebpage(twitchAuthUrl + s)
    }

    private fun createAutorizationUrl(port: Int): String {
        val twitchAuthStateVerify = Calendar.getInstance().timeInMillis

        val scopes = mutableListOf("chat:read+moderator:manage:chat_settings")
        if (state.value.timeout) {
            scopes.add("moderator:manage:banned_users")
        }
        if (state.value.vip) {
            scopes.add("channel:manage:vips")
        }


        val s = "?client_id=" + /*TODO secret client ID*/ "ufhs6qfun32i13ua7uh0mw06l5wz1i" +
                "&" +
                "redirect_uri=" + "http://localhost:$port" +
                "&" +
                "state=" + twitchAuthStateVerify +
                "&" +
                "response_type=token" +
                "&" +
                "scope=" + scopes.joinToString("+")
        return s
    }
}
