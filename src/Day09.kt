fun main() {
    fun readHeights(input: List<String>): Array<Array<Int>> {
        return input.map { it.map { it.digitToInt() }.toTypedArray() }.toTypedArray()
    }

    fun findMinima(heights: Array<Array<Int>>): List<Pair<Int, Int>> {
        val rows = heights.size
        val cols = heights[0].size
        val minima = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                val above = if (i > 0) heights[i - 1][j] else Int.MAX_VALUE
                val below = if (i < rows - 1) heights[i + 1][j] else Int.MAX_VALUE
                val left = if (j > 0) heights[i][j - 1] else Int.MAX_VALUE
                val right = if (j < cols - 1) heights[i][j + 1] else Int.MAX_VALUE
                if (heights[i][j] < above && heights[i][j] < below && heights[i][j] < left && heights[i][j] < right) {
                    minima.add(Pair(i,j))
                }
            }
        }
        return minima
    }

    fun part1(input: List<String>): Int {
        val heights = readHeights(input)
        val minima = findMinima(heights)
        return minima.fold(0) { acc, minimum -> acc + heights[minimum.first][minimum.second] + 1 }
    }

    fun basinSize(
        heights: Array<Array<Int>>,
        rows: Int,
        cols: Int,
        visited: Array<Array<Boolean>>,
        position: Pair<Int, Int>): Int {
        if (position.first < 0 ||
            position.first >= rows ||
            position.second <0 ||
            position.second >= cols ||
            heights[position.first][position.second] == 9 ||
            visited[position.first][position.second])
            return 0
        visited[position.first][position.second] = true
        return 1 +
                basinSize(heights, rows, cols, visited, Pair(position.first-1, position.second)) +
                basinSize(heights, rows, cols, visited, Pair(position.first+1, position.second)) +
                basinSize(heights, rows, cols, visited, Pair(position.first, position.second-1)) +
                basinSize(heights, rows, cols, visited, Pair(position.first, position.second+1))
    }

    data class MinimumAndBasinSize(val minimum: Pair<Int, Int>, val size: Int)

    fun part2(input: List<String>): Int {
        val heights = readHeights(input)
        val minima = findMinima(heights)
        val rows = heights.size
        val cols = heights[0].size
        val minimaAndBasinSizes = minima.map {
            val visited = Array(rows) { Array(cols) { false } }
            MinimumAndBasinSize(it, basinSize(heights, rows, cols, visited, it))
        }.sortedByDescending { it.size }
        return minimaAndBasinSizes[0].size * minimaAndBasinSizes[1].size * minimaAndBasinSizes[2].size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
