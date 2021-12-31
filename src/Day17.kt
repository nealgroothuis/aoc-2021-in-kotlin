import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.math.sqrt

interface ShotResult

val Undershoot = object : ShotResult {}

data class Hit(val maxHeight: Int) : ShotResult
data class Overshoot(val x: Int) : ShotResult

//enum class ShotResult {
//    Undershoot, Hit, Overshoot
//}

fun main() {
    data class Position(val x: Int, val y: Int)
    data class Velocity(val deltaX: Int, val deltaY: Int)
    data class TargetArea(val xMin: Int, val xMax: Int, val yMin: Int, val yMax: Int) {
        fun contains(position: Position): Boolean {
            return xMin <= position.x && xMax >= position.x && yMin <= position.y && yMax >= position.y
        }
    }

    fun nextPositionAndVelocity(position: Position, velocity: Velocity): Pair<Position, Velocity> {
        val nextX = position.x + velocity.deltaX
        val nextY = position.y + velocity.deltaY
        val nextDeltaX = when {
            velocity.deltaX > 0 -> velocity.deltaX - 1
            velocity.deltaX < 0 -> velocity.deltaX + 1
            else -> 0
        }
        val nextDeltaY = velocity.deltaY - 1
        return Pair(Position(nextX, nextY), Velocity(nextDeltaX, nextDeltaY))
    }

    fun calculateShotResult(initialVelocity: Velocity, targetArea: TargetArea): ShotResult {
        var position = Position(0, 0)
        var velocity = initialVelocity
        var maxY = position.y
        while (true) {
            maxY = max(maxY, position.y)
            when {
                targetArea.contains(position) -> return Hit(maxY)
                position.y < targetArea.yMin && velocity.deltaY < 0 && position.x < targetArea.xMin -> return Undershoot
                position.y < targetArea.yMin && velocity.deltaY < 0 -> return Overshoot(position.x)
                else -> {
                    val nextPositionAndVelocity = nextPositionAndVelocity(position, velocity)
                    position = nextPositionAndVelocity.first
                    velocity = nextPositionAndVelocity.second
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val matchResult = """target area: x=([-0-9]*)\.\.([-0-9]*), y=([-0-9]*)\.\.([-0-9]*)"""
            .toRegex()
            .matchEntire(input[0])!!
        val targetArea = TargetArea(
            matchResult.groupValues[1].toInt(),
            matchResult.groupValues[2].toInt(),
            matchResult.groupValues[3].toInt(),
            matchResult.groupValues[4].toInt()
        )
        val minDeltaX = ceil(sqrt(0.25 + 2 * targetArea.xMin) - 0.5).roundToInt()
        var maxHeight = -1
        for (deltaX in minDeltaX..targetArea.xMax) {
            for (deltaY in 1..300) {
                val shotResult = calculateShotResult(Velocity(deltaX, deltaY), targetArea)
                if (shotResult is Hit) {
                    maxHeight = max(maxHeight, shotResult.maxHeight)
                }
            }
        }
        return maxHeight
    }

    fun part2(input: List<String>): Int {
        val matchResult = """target area: x=([-0-9]*)\.\.([-0-9]*), y=([-0-9]*)\.\.([-0-9]*)"""
            .toRegex()
            .matchEntire(input[0])!!
        val targetArea = TargetArea(
            matchResult.groupValues[1].toInt(),
            matchResult.groupValues[2].toInt(),
            matchResult.groupValues[3].toInt(),
            matchResult.groupValues[4].toInt()
        )
        val minDeltaX = ceil(sqrt(0.25 + 2 * targetArea.xMin) - 0.5).roundToInt()
        val hits = mutableListOf<Pair<Int, Int>>()
        for (deltaX in minDeltaX..targetArea.xMax) {
            for (deltaY in targetArea.yMin..400) {
                val shotResult = calculateShotResult(Velocity(deltaX, deltaY), targetArea)
                if (shotResult is Hit) {
                    hits.add(Pair(deltaX, deltaY))
                }
            }
        }
        return hits.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17_test")
    check(part2(testInput) == 112)

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}
