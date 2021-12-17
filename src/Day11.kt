import java.lang.Integer.max
import java.lang.Integer.min

fun main() {
    fun printArray(array: Array<Array<Int>>) {
        for (row in array) {
            println(row.map { it.toString() })
        }
    }

    fun nextPass(rows: Int, cols: Int, energyLevels: Array<Array<Int>>, hasFlashed: Array<Array<Boolean>>): Int {
        var numFlashes = 0
        for (r in 0 until rows) {
            for (c in 0 until cols) {
                if (energyLevels[r][c] > 9 && !hasFlashed[r][c]) {
                    hasFlashed[r][c] = true
                    numFlashes++
                    for (r1 in max(0, r - 1)..min(rows - 1, r + 1)) {
                        for (c1 in max(0, c - 1)..min(cols - 1, c + 1)) {
                            if (!(r1 == r && c1 == c)) {
                                energyLevels[r1][c1]++
                            }
                        }
                    }
                }
            }
        }
        return numFlashes
    }

    fun nextStep(energyLevels: Array<Array<Int>>): Pair<Array<Array<Int>>, Int> {
        val rows = energyLevels.size
        val cols = energyLevels[0].size
        val hasFlashed = Array(rows) { Array(cols) { false } }
        var numFlashed = 0
        val nextEnergyLevels = energyLevels.map { it.map { it + 1 }.toTypedArray() }.toTypedArray()
        var numFlashedThisPass = nextPass(rows, cols, nextEnergyLevels, hasFlashed)
        while (numFlashedThisPass > 0) {
            numFlashed += numFlashedThisPass
            numFlashedThisPass = nextPass(rows, cols, nextEnergyLevels, hasFlashed)
        }
        val resetEnergyLevels = nextEnergyLevels.map { it.map { if (it > 9) 0 else it }.toTypedArray() }.toTypedArray()
        return Pair(resetEnergyLevels, numFlashed)
    }

    fun part1(input: List<String>): Int {
        var energyLevels = input.map { it.toCharArray().map { it.digitToInt() }.toTypedArray() }.toTypedArray()
        var totalFlashes = 0
        for (i in 0 until 100) {
            val nextStepResults = nextStep(energyLevels)
            totalFlashes += nextStepResults.second
            energyLevels = nextStepResults.first
        }
        return totalFlashes
    }

    fun part2(input: List<String>): Int {
        var energyLevels = input.map { it.toCharArray().map { it.digitToInt() }.toTypedArray() }.toTypedArray()
        var step = 1
        var nextStepResults = nextStep(energyLevels)
        while (step < 10000 && nextStepResults.second != 100) {
            step++
            energyLevels = nextStepResults.first
            nextStepResults = nextStep(energyLevels)
        }
        return step
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 1656)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}
