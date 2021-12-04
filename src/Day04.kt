fun main() {
    val cardSize = 5

    class BingoCard(strings: List<String>) {
        val numbers: Array<Array<Int>> =
            strings.map { row -> (row.trim().split(" +".toRegex()).map { it.toInt() }).toTypedArray() }.toTypedArray()
        val marked: Array<Array<Boolean>> = Array(cardSize) { Array(cardSize) { false } }

        fun markNumber(number: Int) {
            for (i in numbers.indices) {
                for (j in numbers[i].indices) {
                    if (numbers[i][j] == number) {
                        marked[i][j] = true
                    }
                }
            }
        }

        fun hasBingo(): Boolean {
            for (i in numbers.indices) {
                if (marked[i].all { it }) return true
            }
            for (j in numbers[0].indices) {
                if (marked.map { it[j] }.all { it }) return true
            }
            return false
        }

        fun sumOfUnmarkedNumbers(): Int {
            var sum = 0
            for (i in marked.indices) {
                for (j in marked[i].indices) {
                    if (!marked[i][j]) {
                        sum += numbers[i][j]
                    }
                }
            }
            return sum
        }
    }

    fun part1(input: List<String>): Int {
        val numbersCalled = input[0].split(",").map { it.toInt() }
        val bingoCardInputs = input.drop(1).chunked(cardSize + 1)
        val bingoCards = bingoCardInputs.map { BingoCard(it.drop(1)) }

        var cardScoreWithBingo: Int? = null
        for (numberCalled in numbersCalled) {
            for (i in bingoCards.indices) {
                bingoCards[i].markNumber(numberCalled)
                if (bingoCards[i].hasBingo()) {
                    cardScoreWithBingo = bingoCards[i].sumOfUnmarkedNumbers() * numberCalled
                }
            }
            if (cardScoreWithBingo != null) break
        }
        return cardScoreWithBingo ?: -1
    }

    fun part2(input: List<String>): Int {
        val numbersCalled = input[0].split(",").map { it.toInt() }
        val bingoCardInputs = input.drop(1).chunked(cardSize + 1)
        val bingoCards = bingoCardInputs.map { BingoCard(it.drop(1)) }

        var cardScoreWithLastBingo: Int? = null
        val cardsWithBingos = Array(bingoCards.size) { false }
        for (numberCalled in numbersCalled) {
            for (i in bingoCards.indices) {
                if (!cardsWithBingos[i]) {
                    bingoCards[i].markNumber(numberCalled)
                    if (bingoCards[i].hasBingo()) {
                        cardsWithBingos[i] = true
                        if (cardsWithBingos.all { it }) cardScoreWithLastBingo =
                            bingoCards[i].sumOfUnmarkedNumbers() * numberCalled
                    }
                }
            }
            if (cardScoreWithLastBingo != null) break
        }
        return cardScoreWithLastBingo ?: -1
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
