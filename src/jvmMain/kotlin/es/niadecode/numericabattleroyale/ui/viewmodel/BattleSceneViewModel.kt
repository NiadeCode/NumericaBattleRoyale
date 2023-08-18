package es.niadecode.numericabattleroyale.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import es.niadecode.numericabattleroyale.model.battle.BattleParticipation
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
import kotlin.math.cos
import kotlin.math.sin

const val CHUNK_SIZE = 20

class BattleSceneViewModel() : ViewModel() {

    private val settingsRepository: SettingsRepository by lazy { SettingsRepository(createPreferences()) }
    private val apiRepository by lazy { TwitchApiRepository(viewModelScope, settingsRepository) }

    private val _soldiers by lazy {
        MutableStateFlow<List<SoldierData>>(emptyList())
    }
    val soldiers: StateFlow<List<SoldierData>> = _soldiers
    var participationList: List<BattleParticipation> = emptyList()

    //var soldiers = mutableStateListOf<SoldierData>()
    var gameState by mutableStateOf(BattleState.START)
    var gameStatus by mutableStateOf("Let's play!")

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
                            size = 20,
                        )
                    )
                }
            }
            _soldiers.value = newSoldiers

        }

        gameState = BattleState.PLAYING
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
    }

    fun endGame() {
        //    gameObjects.remove(ship)
        gameState = BattleState.GLORY

        gameStatus = "GLORY TO ${_soldiers.value.distinctBy { it.name }.first().name}!"

        distributePrizes(_soldiers.value.distinctBy { it.name }.first().name)

    }

    private fun distributePrizes(name: String) {
        viewModelScope.launch {
            participationList.filter {
                it.name != name && it.isMod.not()
            }.forEach {
                viewModelScope.launch {
                    apiRepository.ban(it.userId, it.soldiers * settingsRepository.getBanMultiplier())
                }
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

}
