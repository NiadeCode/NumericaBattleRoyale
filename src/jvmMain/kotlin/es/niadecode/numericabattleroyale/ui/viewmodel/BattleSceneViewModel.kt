package es.niadecode.numericabattleroyale.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpOffset
import es.niadecode.numericabattleroyale.model.battle.BattleParticipation
import es.niadecode.numericabattleroyale.model.battle.SoldierData
import es.niadecode.numericabattleroyale.ui.state.BattleState
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

    private val _soldiers by lazy {
        MutableStateFlow<List<SoldierData>>(emptyList())
    }
    val soldiers: StateFlow<List<SoldierData>> = _soldiers

    //var soldiers = mutableStateListOf<SoldierData>()
    var gameState by mutableStateOf(BattleState.START)
    var gameStatus by mutableStateOf("Let's play!")

    fun startGame(battleParticipations: List<BattleParticipation>, center: Point, radius: Double) {

        println("start game")
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
                    radius = battleParticipation.soldiers.toDouble(),
                    center = spawnPoints[index]
                )

                soldierSpawnPoint.forEach { point: Point ->
                    newSoldiers.add(
                        SoldierData(
                            point = point,
                            name = battleParticipation.name,
                            color = battleParticipation.color,
                            size = 10,
                            chunk = Point(point.x % CHUNK_SIZE, point.y % CHUNK_SIZE),
                        )
                    )
                }
            }
            _soldiers.value = newSoldiers

        }

        //ship.position = Vector2(width.value / 2.0, height.value / 2.0)
        //ship.movementVector = Vector2.ZERO
        //gameObjects.add(ship)
        // repeat(3) {
        //   gameObjects.add(AsteroidData().apply {
        //     position = Vector2(100.0, 100.0); angle = Random.nextDouble() * 360.0; speed = 2.0
        //   })
        //  }
        gameState = BattleState.PLAYING
        gameStatus = "Good luck!"
    }

    fun update() {
        println("update")

        viewModelScope.launch {
            _soldiers.value = _soldiers.value.map { currentSoldier ->

                val enemyChunk = getChunkedEnemis(currentSoldier).map { it.chunk }


                val enemies = soldiers.value
                    .filter { it.chunk in enemyChunk }
                    .filter { it.isEnemy(currentSoldier) }
                    .sortedBy { it.point.distanceSq(currentSoldier.point) }
                enemies.firstOrNull()?.let {
                    currentSoldier.update(it)
                } ?: run { currentSoldier }
            }

            val tmpSoldiers = soldiers.value.shuffled().toMutableList()
            soldiers.value.forEach { currentSoldier ->
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


            // Win condition
            //if (soldiers.isEmpty()) {
            //    winGame()
            //}
        }
    }

    private fun getChunkedEnemis(currentSoldier: SoldierData): List<SoldierData> {
        val enemies = soldiers.value
            .filter { it.isEnemy(currentSoldier) }
            .distinctBy { it.chunk }
            .sortedBy { it.chunk.distanceSq(currentSoldier.chunk) }
            .take(4)


        return enemies
    }

    fun endGame() {
        //    gameObjects.remove(ship)
        gameState = BattleState.GLORY
        gameStatus = "glory to %s!"
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