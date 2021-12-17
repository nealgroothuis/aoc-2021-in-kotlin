fun main() {
    fun buildAdjacencies(input: List<String>): Map<String, Set<String>> {
        val adjacencies = mutableMapOf<String, Set<String>>()
        for (line in input) {
            val caveNames = line.split("-")
            val cave1 = caveNames[0]
            val cave2 = caveNames[1]
            adjacencies[cave1] = adjacencies[cave1].orEmpty().plus(cave2)
            adjacencies[cave2] = adjacencies[cave2].orEmpty().plus(cave1)
        }
        return adjacencies
    }

    fun generatePaths1(
        adjacencies: Map<String, Set<String>>,
        paths: MutableList<List<String>>,
        currentCave: String,
        history: List<String>,
        smallCavesVisited: Set<String>) {
        if (currentCave == "end") {
            paths.add(history)
        } else {
            val nextSmallCavesVisited = if (currentCave[0].isLowerCase()) smallCavesVisited.plus(currentCave) else smallCavesVisited
            for (nextPossibleCave in adjacencies[currentCave]!!) {
                if (!smallCavesVisited.contains(nextPossibleCave)) {
                    generatePaths1(adjacencies, paths, nextPossibleCave, history.plus(currentCave), nextSmallCavesVisited)
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val adjacencies = buildAdjacencies(input)
        val paths = mutableListOf<List<String>>()
        generatePaths1(adjacencies, paths, "start", listOf("start"), emptySet())
       return paths.size
    }

    fun generatePaths2(
        adjacencies: Map<String, Set<String>>,
        paths: MutableList<List<String>>,
        currentCave: String,
        history: List<String>,
        smallCavesVisited: Set<String>,
    smallCaveVisitedTwice: String?) {
        if (currentCave == "end") {
            paths.add(history)
            println(history.joinToString(","))
        } else {
            val nextSmallCavesVisited = if (currentCave[0].isLowerCase()) smallCavesVisited.plus(currentCave) else smallCavesVisited
            val nextSmallCaveVisitedTwice = if (smallCavesVisited.contains(currentCave)) currentCave else smallCaveVisitedTwice
            for (nextPossibleCave in adjacencies[currentCave]!!) {
                if (nextPossibleCave == "start") continue
                else if (!nextSmallCavesVisited.contains(nextPossibleCave) || nextSmallCaveVisitedTwice == null) {
                    generatePaths2(adjacencies, paths, nextPossibleCave, history.plus(currentCave), nextSmallCavesVisited, nextSmallCaveVisitedTwice)
                }
            }
        }
    }

    fun part2(input: List<String>): Int {
        val adjacencies = buildAdjacencies(input)
        val paths = mutableListOf<List<String>>()
        generatePaths2(adjacencies, paths, "start", emptyList(), emptySet(), null)
        return paths.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part2(testInput) == 36)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
