package es.niadecode.numericabattleroyale.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import es.niadecode.numericabattleroyale.model.battle.BattleParticipation
import es.niadecode.numericabattleroyale.model.battle.BattleWidget
import es.niadecode.numericabattleroyale.model.battle.SoldierData
import es.niadecode.numericabattleroyale.repository.SettingsRepository
import es.niadecode.numericabattleroyale.repository.TwitchApiRepository
import es.niadecode.numericabattleroyale.ui.state.BattleState
import es.niadecode.numericabattleroyale.util.createPreferences
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import java.awt.Point
import java.lang.Math.random
import kotlin.math.cos
import kotlin.math.sin

class BattleSceneViewModel(val settingsRepository: SettingsRepository) : ViewModel() {

    private val apiRepository by lazy { TwitchApiRepository(viewModelScope, settingsRepository) }

    private val _soldiers by lazy {
        MutableStateFlow<List<SoldierData>>(emptyList())
    }
    val soldiers: StateFlow<List<SoldierData>> = _soldiers
    var participationList: List<BattleParticipation> = emptyList()

    private val _state = MutableStateFlow(BattleState.START)
    val state: StateFlow<BattleState> = _state

    var gameStatus by mutableStateOf("Let's play!")

    var participantsWidget by mutableStateOf<List<BattleWidget>>(emptyList())

    fun startGame(battleParticipations: List<BattleParticipation>, center: Point, radius: Double) {


        println("start game")
        participationList = battleParticipations
        _soldiers.value = emptyList()

        viewModelScope.launch {
            val spawnPoints = getSpawnPoints(
                points = battleParticipations.size,
                radius = radius,
                center = center
            )

            val newSoldiers = mutableListOf<SoldierData>()
            battleParticipations.forEachIndexed { index, battleParticipation ->
                val soldierSpawnPoint = getSpawnPoints(
                    points = battleParticipation.soldiers,
                    radius = battleParticipation.soldiers.toDouble() * 3,
                    center = spawnPoints[index]
                )

                soldierSpawnPoint.forEachIndexed { soldierIndex, point ->
                    newSoldiers.add(
                        SoldierData(
                            soldierID = "${battleParticipation.name}$soldierIndex",
                            point = point,
                            name = battleParticipation.name,
                            color = battleParticipation.color,
                            size = 30,
                        )
                    )
                }
            }
            _soldiers.value = newSoldiers

        }

        _state.value = BattleState.PLAYING
        gameStatus = "Good luck!"
    }

    fun update() {
        println("update")

        viewModelScope.launch {
            _soldiers.value = _soldiers.value.map { currentSoldier ->
                val objetive = soldiers.value.find { it.soldierID == currentSoldier.objetive }
                if (currentSoldier.objetive.isNotEmpty() && objetive != null) {
                    currentSoldier.update(objetive)
                } else {
                    soldiers.value
                        .filter { it.isEnemy(currentSoldier) }
                        .map { soldierData ->
                            viewModelScope.async {
                                Pair(soldierData, soldierData.point.distance(currentSoldier.point))
                            }
                        }
                        .awaitAll().minByOrNull { it.second }
                        ?.let {
                            currentSoldier.update(it.first)
                        } ?: run { currentSoldier }
                }
            }

            val tmpSoldiers = soldiers.value.toMutableList()
            soldiers.value.shuffled().forEach { currentSoldier ->
                if (tmpSoldiers.isNotEmpty() && tmpSoldiers.contains(currentSoldier)) {
                    val leash = soldiers.value
                        .filter { it.isEnemy(currentSoldier) }
                        .firstOrNull { it.overlapsWith(currentSoldier) }
                    leash?.let {
                        tmpSoldiers.remove(it)
                    }
                }
            }
            _soldiers.value = tmpSoldiers


            //Win condition
            if (_soldiers.value.distinctBy { it.name }.size == 1) {
                endGame()
            }

        }

        viewModelScope.launch {
            participantsWidget = participationList
                .mapNotNull { participant ->
                    val soldiers = _soldiers.value.filter { it.name == participant.name }.size
                    if (soldiers > 0) {
                        BattleWidget(
                            userName = participant.name,
                            soldiers = soldiers,
                            color = participant.color
                        )
                    } else null
                }
                .sortedByDescending { it.soldiers }
                .take(10)
        }
    }

    fun endGame() {
        _state.value = BattleState.GLORY
        gameStatus = "GLORY TO ${_soldiers.value.distinctBy { it.name }.first().name}!"
        distributePrizes(_soldiers.value.distinctBy { it.name }.first().name)
    }

    private fun distributePrizes(name: String) {
        println("distributePrizes")
        viewModelScope.launch {
            val modsImmunity = settingsRepository.getModsImmunity()
            participationList.filter {
                it.name != name
            }
                .filter {
                    if (modsImmunity)
                        it.isMod.not()
                    else true
                }
                .forEach {
                    viewModelScope.launch {
                        println("ban ${it.name} ${it.userId}")
                        apiRepository.ban(it.userId, it.soldiers * settingsRepository.getBanMultiplier())
                    }
                }
            val winner = participationList.find { it.name == name }
            winner?.let {
                apiRepository.vip(winner.userId)
                settingsRepository.setVipUserId(winner.userId)
                settingsRepository.setVipUserName(winner.name)
            }
        }
    }


    fun getSpawnPoints(points: Int, radius: Double, center: Point): List<Point> {
        val slice: Double = 2 * kotlin.math.PI / points
        val spawnpoints = mutableStateListOf<Point>()
        for (i in 0 until points) {
            val angle = slice * i
            val newX = (center.x + radius * cos(angle)).toInt()
            val newY = (center.y + radius * sin(angle)).toInt()
            spawnpoints.add(Point(newX, newY))
            println(radius)
        }
        return spawnpoints
    }

    fun getSoldiersPoints(points: Int, center: Point): List<Point> {
        val level = 5
        val slice: Double = 2 * level * kotlin.math.PI / points
        val spawnpoints = mutableStateListOf<Point>()
        for (currentLevel in 0 until level) {
            val subradio = 100 * (currentLevel + 1) / level
            for (i in 0 until points / level) {
                val angle = slice * i + (level * (0..500).random())
                val newX = (center.x + subradio * cos(angle)).toInt()
                val newY = (center.y + subradio * sin(angle)).toInt()
                spawnpoints.add(Point(newX, newY))
            }
        }
        return spawnpoints.take(points)
    }

}
