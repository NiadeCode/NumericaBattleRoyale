package es.niadecode.numericabattleroyale.ui.viewmodel

import es.niadecode.numericabattleroyale.model.battle.BattleParticipation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import moe.tlaster.precompose.viewmodel.ViewModel

class MainViewModel() : ViewModel() {

    private val _battleParticipations = MutableStateFlow<List<BattleParticipation>>(emptyList())
    val battleParticipations: StateFlow<List<BattleParticipation>> = _battleParticipations

    fun setNewParticipations(battleParticipations: List<BattleParticipation>) {
        _battleParticipations.value = battleParticipations
    }

}