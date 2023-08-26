package es.niadecode.numericabattleroyale.ui.viewmodel

import es.niadecode.numericabattleroyale.model.config.ConfigState
import es.niadecode.numericabattleroyale.repository.SettingsRepository
import es.niadecode.numericabattleroyale.repository.TwitchApiRepository
import es.niadecode.numericabattleroyale.repository.TwitchLoginRepository
import es.niadecode.numericabattleroyale.repository.twitchAuthUrl
import es.niadecode.numericabattleroyale.util.openWebpage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import java.util.*

class HomeSceneViewModel(val settingsRepository: SettingsRepository) : ViewModel() {

    private val apiRepository by lazy { TwitchApiRepository(viewModelScope, settingsRepository) }
    private val loginRepository by lazy { TwitchLoginRepository(viewModelScope, settingsRepository) }

    private val _state by lazy {
        MutableStateFlow(
            ConfigState(
                timeout = settingsRepository.getBan(),
                timeoutMultiplier = settingsRepository.getBanMultiplier(),
                vip = settingsRepository.getVip(),
                modImmunity = settingsRepository.getModsImmunity(),
                isConnected = false,
                batleOnFail = settingsRepository.getBattleOnFail(),
                maxParticipations = settingsRepository.getmaxParticipation(),
            )
        )
    }

    val state: StateFlow<ConfigState> = _state

    private val _navigate = MutableStateFlow(false)
    val navigate: StateFlow<Boolean> = _navigate

    override fun onCleared() {
        super.onCleared()
        loginRepository.stopServer()
    }

    fun setTimeouts(newTimeoutsBoolean: Boolean) {
        _state.value = state.value.copy(timeout = newTimeoutsBoolean)
    }

    fun setVip(vip: Boolean) {
        _state.value = state.value.copy(vip = vip)
    }

    fun setBattleOnFail(vip: Boolean) {
        _state.value = state.value.copy(batleOnFail = vip)
    }

    fun setModImmunity(newModImmunity: Boolean) {
        _state.value = state.value.copy(modImmunity = newModImmunity)
    }

    fun connectTwitch() {
        if (state.value.isConnected) {
            viewModelScope.launch {
                _navigate.value = true
                delay(50)
                _navigate.value = false
                saveSettings()
            }
        } else {
            val port = 38769
            val s = createAutorizationUrl(port)
            loginRepository.startServer(port)
            saveSettings()

            viewModelScope.launch {
                loginRepository.tokenFlow.collect {
                    apiRepository.validate().collect {
                        _navigate.value = true
                        delay(50)
                        _state.value = _state.value.copy(isConnected = true)
                        _navigate.value = false
                    }
                }
            }

            openWebpage(twitchAuthUrl + s)
        }
    }

    private fun saveSettings() {
        viewModelScope.launch {
            val state = state.value
            settingsRepository.setBan(state.timeout)
            settingsRepository.setBanMultiplier(state.timeoutMultiplier)
            settingsRepository.setVip(state.vip)
            settingsRepository.setMod(state.modImmunity)
            settingsRepository.setMaxParticipation(state.maxParticipations)
            settingsRepository.setBattleOnFail(state.batleOnFail)
        }
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


    fun updateTimeoutMultiplier(multiplier: Int?) {
        multiplier?.let {
            _state.value = _state.value.copy(timeoutMultiplier = it)
        }
    }

    fun updateMax(max: Int?) {
        max?.let {
            _state.value = _state.value.copy(maxParticipations = it)
        }
    }
}
