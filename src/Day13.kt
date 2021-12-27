fun main() {
    fun readDotArray(input: List<String>): Array<Array<Boolean>> {
        val dots = input.map {
            val coordStrings = it.split(",")
            Pair(coordStrings[0].toInt(), coordStrings[1].toInt())
        }.toSet()
        val maxX = dots.maxOf { it.first }
        val maxY = dots.maxOf { it.second }
        return Array(maxY + 1) { x -> Array(maxX + 1) { y -> dots.contains(Pair(y, x)) } }
    }

    fun printDotArray(dotArray: Array<Array<Boolean>>) {
        for (row in dotArray) {
            for (hasDot in row) {
                if (hasDot) print("#") else print(".")
            }
            println()
        }
    }

    data class FoldInstruction(val axis: Char, val along: Int)
    fun readFoldInstructions(input: List<String>): List<FoldInstruction> {
        return input.map { it.replace("fold along ", "")}.map {
            val instructionStrings=it.split("=")
            FoldInstruction(axis = instructionStrings[0][0], along=instructionStrings[1].toInt())
        }
    }

    fun foldUp(dotArray: Array<Array<Boolean>>, along: Int): Array<Array<Boolean>> {
        val numRows = dotArray.size
        val numCols = dotArray[0].size
        val foldedDotArray = Array(along) { y -> Array(numCols) { x -> dotArray[y][x]} }
        for (j in along + 1 until numRows) {
            for (i in 0 until numCols) {
                foldedDotArray[2 * along - j][i] = foldedDotArray[2 * along - j][i] || dotArray[j][i]
            }
        }
        return foldedDotArray
    }

    fun foldLeft(dotArray: Array<Array<Boolean>>, along: Int): Array<Array<Boolean>> {
        val numRows = dotArray.size
        val numCols = dotArray[0].size
        val foldedDotArray = Array(numRows) { y -> Array(along) { x -> dotArray[y][x]} }
        for (j in 0 until numRows) {
            for (i in along + 1 until numCols) {
                foldedDotArray[j][2 * along - i] = foldedDotArray[j][2 * along - i] || dotArray[j][i]
            }
        }
        return foldedDotArray
    }

    fun numDotsInArray(dotArray: Array<Array<Boolean>>): Int {
      return dotArray.fold(0) { acc, dots -> acc + dots.count { it }}
    }

    fun part1(input: List<String>): Int {
        val dotArray = readDotArray(input.takeWhile { it.isNotEmpty() })
        val foldInstructions = readFoldInstructions(input.dropWhile { it.isNotEmpty() }.drop(1))
        val finalDotArray = foldInstructions.take(1).fold(dotArray) { acc, foldInstruction -> when (foldInstruction.axis) {
            'x' -> foldLeft(acc, foldInstruction.along)
            'y' -> foldUp(acc, foldInstruction.along)
            else -> throw RuntimeException("Invalid fold axis: ${foldInstruction.axis}")
        } }
        printDotArray(finalDotArray)
        return numDotsInArray(finalDotArray)
    }

    fun part2(input: List<String>): Int {
        val dotArray = readDotArray(input.takeWhile { it.isNotEmpty() })
        val foldInstructions = readFoldInstructions(input.dropWhile { it.isNotEmpty() }.drop(1))
        val finalDotArray = foldInstructions.fold(dotArray) { acc, foldInstruction -> when (foldInstruction.axis) {
            'x' -> foldLeft(acc, foldInstruction.along)
            'y' -> foldUp(acc, foldInstruction.along)
            else -> throw RuntimeException("Invalid fold axis: ${foldInstruction.axis}")
        } }
        printDotArray(finalDotArray)
        return numDotsInArray(finalDotArray)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
