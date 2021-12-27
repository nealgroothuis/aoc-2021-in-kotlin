fun main() {

    data class MinRisk(val minRisk: Int, val inboundDirection: String?)

    fun findMinRisk(risks: Array<Array<Int>>): Int {
        val numRows = risks.size
        val numCols = risks[0].size
        val minRisks = Array(numRows) { Array<MinRisk?>(numCols) { null } }
        minRisks[0][0] = MinRisk(0, null)
        var minRisksWasChanged = true
        while (minRisksWasChanged) {
            minRisksWasChanged = false
            for (r in 0 until numRows) {
                for (c in 0 until numCols) {
                    val minRisk = minRisks[r][c]?.minRisk
                    if (r > 0) {
                        val minRiskAbove = minRisks[r - 1][c]?.minRisk
                        if (minRiskAbove != null && (minRisk == null || minRisk > minRiskAbove + risks[r][c])) {
                            minRisks[r][c] = MinRisk(minRiskAbove + risks[r][c], "above")
                            minRisksWasChanged = true
                        }
                    }
                    if (r < numRows - 1) {
                        val minRiskBelow = minRisks[r + 1][c]?.minRisk
                        if (minRiskBelow != null && (minRisk == null || minRisk > minRiskBelow + risks[r][c])) {
                            minRisks[r][c] = MinRisk(minRiskBelow + risks[r][c], "below")
                            minRisksWasChanged = true
                        }
                    }
                    if (c > 0) {
                        val minRiskLeft = minRisks[r][c - 1]?.minRisk
                        if (minRiskLeft != null && (minRisk == null || minRisk > minRiskLeft + risks[r][c])) {
                            minRisks[r][c] = MinRisk(minRiskLeft + risks[r][c], "left")
                            minRisksWasChanged = true
                        }
                    }
                    if (c < numCols - 1) {
                        val minRiskRight = minRisks[r][c + 1]?.minRisk
                        if (minRiskRight != null && (minRisk == null || minRisk > minRiskRight + risks[r][c])) {
                            minRisks[r][c] = MinRisk(minRiskRight + risks[r][c], "right")
                            minRisksWasChanged = true
                        }
                    }
                }
            }
        }
        return minRisks[numRows - 1][numCols - 1]?.minRisk!!
    }

    fun part1(input: List<String>): Int {
        val risks = input.map { it.map { it.digitToInt() }.toTypedArray() }.toTypedArray()
        return findMinRisk(risks)
    }

    fun part2(input: List<String>): Int {
        val risks = input.map { it.map { it.digitToInt() }.toTypedArray() }.toTypedArray()
        val numRows = risks.size
        val numCols = risks[0].size
        val risksCopiedRight =
            Array(numRows) { i -> Array(numCols * 5) { j -> (risks[i][j % numCols] + j / numCols - 1) % 9 + 1 } }
        val risksCopiedRightAndDown =
            Array(numRows * 5) { i -> Array(numCols * 5) { j -> (risksCopiedRight[i % numRows][j] + i / numRows - 1) % 9 + 1 } }
        return findMinRisk(risksCopiedRightAndDown)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part2(testInput) == 315)

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}
