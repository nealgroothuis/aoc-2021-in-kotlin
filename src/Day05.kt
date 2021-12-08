import java.lang.Integer.max
import java.lang.Integer.min

fun main() {
    class VentLine(val x1: Int, val y1: Int, val x2: Int, val y2: Int) {
        val isHorizontal = y1 == y2
        val isVertical = x1 == x2

        fun generateCoordinates(countDiagonals: Boolean): List<Pair<Int, Int>> {
            return when (Pair(isHorizontal, isVertical)) {
                Pair(true, false) -> (min(x1, x2)..max(x1, x2)).map { Pair(it, y1) }
                Pair(false, true) -> (min(y1, y2)..max(y1, y2)).map { Pair(x1, it) }
                else -> {
                    return if (countDiagonals) {
                        val xs = if (x1 < x2) x1..x2 else x1.downTo(x2)
                        val ys = if (y1 < y2) y1..y2 else y1.downTo(y2)
                        xs.zip(ys)
                    } else emptyList()
                }
            }
        }

        override fun toString(): String {
            return "({$x1}, {$y1}) -> ({$x2}, {$y2})"
        }
    }

    fun countCellsWithAtLeastTwoVents(input: List<String>, countDiagonals: Boolean): Int {
        val gridSize = 1000
        val numVents = Array(gridSize) { Array(gridSize) { 0 } }
        for (inputLine in input) {
            val coordinates =
                inputLine.split(" *-> *".toRegex()).map { it.split(" *, *".toRegex()).map { it.toInt() } }
            val ventLine = VentLine(coordinates[0][0], coordinates[0][1], coordinates[1][0], coordinates[1][1])
            for (coordinate in ventLine.generateCoordinates(countDiagonals)) {
                numVents[coordinate.second][coordinate.first]++
            }
        }
        var cellsWithAtLeastTwoVents = 0
        for (i in 0 until gridSize) {
            for (j in 0 until gridSize) {
                if (numVents[i][j] >= 2) {
                    cellsWithAtLeastTwoVents++
                }
            }
        }
        return cellsWithAtLeastTwoVents
    }

    fun part1(input: List<String>): Int {
      return countCellsWithAtLeastTwoVents(input, false)
    }

    fun part2(input: List<String>): Int {
        return countCellsWithAtLeastTwoVents(input, true)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
