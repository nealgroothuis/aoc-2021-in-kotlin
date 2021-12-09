fun main() {
    data class Entry(
        // length 10
        val signalPatterns: List<Set<Char>>,
        // length 4
        val outputs: List<Set<Char>>
    )

    fun part1(input: List<String>): Int {
        val entries: List<Entry> = input.map { line ->
            val parts = line.split(" \\| ".toRegex())
            Entry(
                signalPatterns = parts[0].split(" ").map { it.toCharArray().toSet() },
                outputs = parts[1].split(" ").map { it.toCharArray().toSet() }
            )
        }
        val uniqueLengths = setOf(2, 3, 4, 7)
        return entries.fold(0) { acc: Int, entry: Entry -> acc + entry.outputs.filter { uniqueLengths.contains(it.size) }.size }
    }

    fun decodeEntry(entry: Entry): List<Int> {
        val signalPatterns = Array(10) { setOf<Char>() }
        signalPatterns[1] = entry.signalPatterns.find { it.size == 2 }!!
        signalPatterns[4] = entry.signalPatterns.find { it.size == 4 }!!
        signalPatterns[7] = entry.signalPatterns.find { it.size == 3 }!!
        signalPatterns[8] = entry.signalPatterns.find { it.size == 7 }!!
        val topLeftAndMiddleSegments = signalPatterns[4].minus(signalPatterns[1])
        val signalPatternsZeroSixAndNine = entry.signalPatterns.filter { it.size == 6 }
        signalPatterns[0] = signalPatternsZeroSixAndNine.find { topLeftAndMiddleSegments.minus(it).isNotEmpty() }!!
        val middleSegment = signalPatterns[8].minus(signalPatterns[0]).toTypedArray()[0]
        val topLeftSegment = topLeftAndMiddleSegments.minus(middleSegment).toTypedArray()[0]
        val signalPatternsTwoThreeAndFive = entry.signalPatterns.filter { it.size == 5 }
        signalPatterns[5] = signalPatternsTwoThreeAndFive.find { topLeftAndMiddleSegments.minus(it).isEmpty() }!!
        val topRightSegment = signalPatterns[1].minus(signalPatterns[5]).toTypedArray()[0]
        signalPatterns[6] = signalPatternsZeroSixAndNine.find { !it.contains(topRightSegment) }!!
        signalPatterns[9] =
            signalPatternsZeroSixAndNine.find { !setOf(signalPatterns[0], signalPatterns[6]).contains(it) }!!
        signalPatterns[3] = signalPatternsTwoThreeAndFive.find { signalPatterns[1].minus(it).isEmpty() }!!
        signalPatterns[2] =
            signalPatternsTwoThreeAndFive.find { !setOf(signalPatterns[3], signalPatterns[5]).contains(it) }!!

        return entry.outputs.map { signalPatterns.indexOf(it) }
    }

    fun part2(input: List<String>): Int {
        val entries: List<Entry> = input.map { line ->
            val parts = line.split(" \\| ".toRegex())
            Entry(
                signalPatterns = parts[0].split(" ").map { it.toCharArray().toSet() },
                outputs = parts[1].split(" ").map { it.toCharArray().toSet() }
            )
        }
        return entries.fold(0) { acc, entry -> acc + decodeEntry(entry).fold(0) { acc, i -> acc * 10 + i } }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
