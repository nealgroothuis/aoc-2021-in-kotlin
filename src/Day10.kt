fun main() {
    val delimiters = setOf(Pair('[', ']'), Pair('(', ')'), Pair('{', '}'), Pair('<', '>'))
    val openingCharacters = delimiters.map { it.first }.toSet()
    val illegalCharScores = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)

    fun unclosedDelimitersAndFirstIllegalChar(line: String): Pair<List<Char>, Char?> {
        val openingCharStack = mutableListOf<Char>()
        for (c in line.toList()) {
            if (openingCharacters.contains(c)) {
                openingCharStack.add(0, c)
            } else if (openingCharStack.isNotEmpty() && delimiters.contains(Pair(openingCharStack[0], c))) {
                openingCharStack.removeAt(0)
            } else {
                return Pair(openingCharStack, c)
            }
        }
        return Pair(openingCharStack, null)
    }

    fun part1(input: List<String>): Int {
        return input
            .map { unclosedDelimitersAndFirstIllegalChar(it) }
            .filter { it.second != null }
            .sumOf { illegalCharScores[it.second]!! }
    }

    val completionCharScore = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)

    fun completionStringScore(completionString: String): Long {
        return completionString.fold(0L) { acc, c -> acc * 5 + completionCharScore[c]!! }
    }

    fun part2(input: List<String>): Long {
        val unclosedDelimiters = input
            .map { unclosedDelimitersAndFirstIllegalChar(it) }
            .filter { it.second == null }
            .map { it.first }
        val completionStrings = unclosedDelimiters.map {
            it.map { ud -> delimiters.find { it.first == ud }!!.second }.joinToString("")
        }
        val completionStringScores = completionStrings.map { completionStringScore(it) }.sorted()
        return completionStringScores[completionStringScores.size / 2]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
