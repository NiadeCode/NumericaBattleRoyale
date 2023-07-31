package es.niadecode.numericabattleroyale.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpOffset
import es.niadecode.numericabattleroyale.model.battle.BattleParticipation
import es.niadecode.numericabattleroyale.model.battle.SoldierData
import es.niadecode.numericabattleroyale.ui.state.BattleState
import java.awt.Point
import kotlin.math.cos
import kotlin.math.sin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import moe.tlaster.precompose.viewmodel.ViewModel

class BattleSceneViewModel() : ViewModel() {

    var prevTime = 0L

    var targetLocation: DpOffset by mutableStateOf(DpOffset.Zero)

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

        val spawPoits = getSpawnPoints(
            points = battleParticipations.size,
            radius = radius,
            center = center
        )

        _soldiers.value = battleParticipations.mapIndexed { index, battleParticipation ->
            val spawnPoint = spawPoits[index]
            //TODO test
            SoldierData(spawnPoint, battleParticipation.name, battleParticipation.color, 5)
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

    fun update(time: Long) {
        println("update")
        val delta = time - prevTime
        val floatDelta = (delta / 1E8).toFloat()
        prevTime = time

        _soldiers.value = _soldiers.value.map { currentSoldier ->

            val enemies = soldiers.value
                .filter { it.name != currentSoldier.name }
                .sortedBy { it.point.distanceSq(currentSoldier.point) }
            val nearestEnemy = enemies.first()

            currentSoldier.update(nearestEnemy)
        }

        val tmpSoldiers = soldiers.value.toMutableList()
        soldiers.value.forEach { currentSoldier ->
            val leash = soldiers.value
                .filter { it.name != currentSoldier.name }
                .firstOrNull { it.overlapsWith(currentSoldier) }
            leash?.let {
                tmpSoldiers.remove(it)
            }
        }
        _soldiers.value = tmpSoldiers


        // Win condition
        //if (asteroids.isEmpty()) {
        //    winGame()
        //}
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