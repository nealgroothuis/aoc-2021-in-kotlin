fun main() {
    fun binaryArrayToInt(list: List<Int>): Int {
        var acc = 0
        for (i in list.indices) {
            acc = acc.shl(1)
            if (list[i] == 1) acc++
        }
        return acc
    }

    fun getGammaBits(input: List<String>): List<Int> {
        val numberLen = input[0].length
        val numOnes = Array(numberLen) { 0 }
        for (number in input) {
            for (i in 0 until numberLen) {
                if (number[i] == '1') {
                    numOnes[i]++
                }
            }
        }
        return List(numberLen) { i -> if (numOnes[i] * 2 >= input.size) 1 else 0 }
    }

    fun part1(input: List<String>): Int {
        val gammaBits = getGammaBits(input)
        val epsilonBits = gammaBits.map { i -> if (i == 1) 0 else 1 }
        val gamma = binaryArrayToInt(gammaBits)
        val epsilon = binaryArrayToInt(epsilonBits)
        return gamma * epsilon
    }

    fun filterOnBits(input: List<String>, useEpsilon: Boolean): Int {
        var candidates = input
        var i = 0
        while (candidates.size > 1) {
            val gammaBits = getGammaBits(candidates)
            val bitFilter = if (useEpsilon) gammaBits.map { i -> if (i == 1) 0 else 1 } else gammaBits
            candidates = candidates.filter { c -> c[i].digitToInt() == bitFilter[i] }
            i++
        }
        return binaryArrayToInt(candidates[0].map { c -> if (c == '1') 1 else 0 })
    }

    fun part2(input: List<String>): Int {
        val oxygenGeneratorRating = filterOnBits(input, false)
        val co2ScrubberRating = filterOnBits(input, true)
        return oxygenGeneratorRating * co2ScrubberRating
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
