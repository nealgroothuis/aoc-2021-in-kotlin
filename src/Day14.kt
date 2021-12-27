fun main() {
    fun iterate(pairInsertionRules: Map<String, Char>, polymer: String): String {
        val newPolymer = StringBuilder()
        for (i in 0 until polymer.length - 1) {
            newPolymer.append(polymer[i])
            val elementToInsert = pairInsertionRules.get(polymer.substring(i, i + 2))
            if (elementToInsert != null) {
                newPolymer.append(elementToInsert)
            }
        }
        newPolymer.append(polymer.last())
        return newPolymer.toString()
    }

    fun part1(input: List<String>): Int {
        val polymerTemplate = input[0]
        val pairInsertionRules = input.drop(2).associate {
            val ruleStrings = it.split(" -> ")
            ruleStrings[0] to ruleStrings[1][0]
        }
        var polymer = polymerTemplate
        for (i in 1..10) {
            polymer = iterate(pairInsertionRules, polymer)
        }
        val frequencies = mutableMapOf<Char, Int>()
        for (e in polymer) {
            frequencies[e] = (frequencies[e] ?: 0) + 1
        }
        val mostFrequentElement = frequencies.maxByOrNull { it.value }!!
        val leastFrequentElement = frequencies.minByOrNull { it.value }!!
        return mostFrequentElement.value - leastFrequentElement.value
    }

    fun iteratePairs(pairInsertionRules: Map<String, Char>, pairs: Map<String, Long>): Map<String, Long> {
        val newPairs = pairs.toMutableMap()
        for (pair in pairs) {
            val charToInsert = pairInsertionRules[pair.key]
            if (charToInsert != null) {
                newPairs[pair.key] = (newPairs[pair.key] ?: 0) - pair.value
                newPairs[(pair.key[0].toString() + charToInsert)] =
                    (newPairs[(pair.key[0].toString() + charToInsert)] ?: 0) + pair.value
                newPairs[(charToInsert + pair.key[1].toString())] =
                    (newPairs[(charToInsert + pair.key[1].toString())] ?: 0) + pair.value
            }
        }
        return newPairs
    }

    fun part2(input: List<String>, numSteps: Int): Long {
        val polymerTemplate = input[0]
        val pairInsertionRules = input.drop(2).associate {
            val ruleStrings = it.split(" -> ")
            ruleStrings[0] to ruleStrings[1][0]
        }
        val pairs = mutableMapOf<String, Long>()
        for (i in 0 until polymerTemplate.length - 1) {
            pairs[polymerTemplate.substring(i, i + 2)] = (pairs[polymerTemplate.substring(i, i + 2)] ?: 0) + 1
        }
        var iteratedPairs: Map<String, Long> = pairs
        for (i in 1..numSteps) {
            println(iteratedPairs)
            iteratedPairs = iteratePairs(pairInsertionRules, iteratedPairs)
        }
        val elementsInPairs = mutableMapOf<Char, Long>()
        for (pair in iteratedPairs) {
            elementsInPairs[pair.key[0]] = (elementsInPairs[pair.key[0]] ?: 0) + pair.value
            elementsInPairs[pair.key[1]] = (elementsInPairs[pair.key[1]] ?: 0) + pair.value
        }
        val elementFrequency = elementsInPairs.mapValues { it.value / 2 + it.value % 2 }
        val leastFrequentElement = elementFrequency.minByOrNull { it.value }!!
        val mostFrequentElement = elementFrequency.maxByOrNull { it.value }!!
        return mostFrequentElement.value - leastFrequentElement.value
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part2(testInput, 10) == 1588L)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input, 40))
}
