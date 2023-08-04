package es.niadecode.numericabattleroyale.model.battle

import androidx.compose.ui.graphics.Color
import java.awt.Point

data class SoldierData(
    var point: Point,
    val name: String,
    val color: Color,
    val size: Int,
    val chunk: Point,
    val model: Int = (1..3).random()
) {

    fun update(objective: SoldierData): SoldierData {

        val (newX, newY) = updateMovement(objective)
        println("oldX = ${point.x}, enemy X = ${objective.point.x}")
        println("newX = ${newX}")
        println("oldY = ${point.y}, enemy Y = ${objective.point.y}")
        println("newY = ${newY}")

        return this.copy(point = Point(newX, newY))
    }

    fun overlapsWith(other: SoldierData): Boolean {
        // Overlap means the center of the game objects are closer together than the sum of their radiuses
        val distance =  this.point.distance(other.point)

        println("$this is distant $distance from $other")
        return distance < (this.size)
    }

    fun isEnemy(other: SoldierData): Boolean {
        return this.name != other.name
    }

    private fun updateMovement(objective: SoldierData): Pair<Int, Int> {
        val newX = if (point.x > objective.point.x) {
            point.x - 1
        } else if (point.x < objective.point.x) {
            point.x + 1
        } else {
            point.x
        }

        val newY = if (point.y > objective.point.y) {
            point.y - 1
        } else if (point.y < objective.point.y) {
            point.y + 1
        } else {
            point.y
        }
        return Pair(newX, newY)
    }
}
