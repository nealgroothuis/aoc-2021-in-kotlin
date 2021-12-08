fun main() {
    fun numLanternFish(input: List<String>, numDays: Int): Long {
        var numFishAtDay = Array<Long>(9) {0}
        input[0].split(",").map { it.toInt() }.forEach { numFishAtDay[it] ++ }

        for (day in 0 until numDays) {
            val newNumFishAtDay = Array<Long>(9) { 0 }
            for (i in 1 until 9) {
                newNumFishAtDay[i - 1] = numFishAtDay[i]
            }
            newNumFishAtDay[8] = numFishAtDay[0]
            newNumFishAtDay[6] += numFishAtDay[0]
            numFishAtDay = newNumFishAtDay
        }
        return numFishAtDay.fold(0) { acc, n -> acc + n }
    }

    fun part1(input: List<String>): Long {
        return numLanternFish(input, 80)
    }

    fun part2(input: List<String>): Long {
        return numLanternFish(input, 256)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934L)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
