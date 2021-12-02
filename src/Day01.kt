fun main() {
    fun part1(input: List<String>): Int {
        var numIncreases = 0
        for (i in 0..(input.size - 2)) {
            if (input[i].toInt() < input[i + 1].toInt())
                numIncreases++
        }
        return numIncreases
    }

    fun part2(input: List<String>): Int {
        var numIncreases = 0
        for (i in 0..(input.size - 4)) {
            if (input[i].toInt() < input[i + 3].toInt())
                numIncreases++
        }
        return numIncreases
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
