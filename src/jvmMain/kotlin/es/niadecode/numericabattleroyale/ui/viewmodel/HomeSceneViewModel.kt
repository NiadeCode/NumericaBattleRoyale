package es.niadecode.numericabattleroyale.ui.viewmodel

import es.niadecode.numericabattleroyale.model.config.ConfigState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import moe.tlaster.precompose.viewmodel.ViewModel

class HomeSceneViewModel() : ViewModel() {

    private val _state by lazy {
        MutableStateFlow(ConfigState(false, 0, false))
    }
    val state: StateFlow<ConfigState> = _state

    override fun onCleared() {
        super.onCleared()
//        stopServer()
    }

    fun setTimeouts(newTimeoutsBoolean: Boolean) {
        _state.value = state.value.copy(timeout = newTimeoutsBoolean)
    }

    fun setTimeoutsMultiplier(newMultiplier: Int) {
        _state.value = state.value.copy(timeoutMultiplier = newMultiplier)
    }

    fun setModInmunity(newModImmunity: Boolean) {
        _state.value = state.value.copy(modImmunity = newModImmunity)
    }
}