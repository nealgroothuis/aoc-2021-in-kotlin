import java.lang.Math.abs
import java.util.Collections.max

fun main() {
    fun part1(input: List<String>): Int {
        val positions = input[0].split(",").map { it.toInt() }
        val n = positions.size
        val maxPosition = max(positions)
        val numSubsAtPosition = Array(maxPosition + 1) { 0 }
        for (position in positions) {
            numSubsAtPosition[position]++
        }
        var d = positions.fold(0) { acc, p -> acc + p }
        var nBeforeI = 0
        var i = 0
        var change = 2 * (nBeforeI + numSubsAtPosition[i]) - n
        while (change < 0) {
            nBeforeI += numSubsAtPosition[i]
            i++
            d += change
            change = 2 * (nBeforeI + numSubsAtPosition[i]) - n
        }
        return d
    }

    fun part2(input: List<String>): Int {
        val positions = input[0].split(",").map { it.toInt() }
        val maxPosition = max(positions)
        val numSubsAtPosition = Array(maxPosition + 1) { 0 }
        for (position in positions) {
            numSubsAtPosition[position]++
        }
        val distance = Array(maxPosition + 1) { 0 }
        for (k in numSubsAtPosition.indices) {
            distance[k] = (0..maxPosition)
                .map { numSubsAtPosition[it] * ((it - k) * (it - k) + abs(it - k)) / 2 }
                .fold(0) { acc, i -> acc + i }
        }
        var minDistance = distance[0]
        for (k in distance.indices) {
            if (distance[k] < minDistance) {
                minDistance = distance[k]
            }
        }
        return minDistance
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
