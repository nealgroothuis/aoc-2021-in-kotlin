import kotlin.math.max

data class ExplodeResult(
    val number: SnailfishOrRegularNumber,
    val exploded: Boolean,
    val addToLeft: Int?,
    val addToRight: Int?
)

data class SplitResult(
    val number: SnailfishOrRegularNumber,
    val split: Boolean
)

sealed interface SnailfishOrRegularNumber {
    fun explode(depth: Int): ExplodeResult
    fun addToLeftmostNumber(value: Int): SnailfishOrRegularNumber
    fun addToRightmostNumber(value: Int): SnailfishOrRegularNumber
    fun split(): SplitResult
    fun magnitude(): Int
}

data class RegularNumber(val number: Int) : SnailfishOrRegularNumber {
    override fun explode(depth: Int): ExplodeResult {
        return ExplodeResult(this, false, null, null)
    }

    override fun addToLeftmostNumber(value: Int): SnailfishOrRegularNumber {
        return RegularNumber(number + value)
    }

    override fun addToRightmostNumber(value: Int): SnailfishOrRegularNumber {
        return RegularNumber(number + value)
    }

    override fun split(): SplitResult {
        if (number >= 10) {
            return SplitResult(
                SnailfishNumber(
                    RegularNumber(number / 2),
                    RegularNumber(number / 2 + number % 2)
                ),
                split = true
            )
        }
        return SplitResult(this, split = false)
    }

    override fun magnitude(): Int {
        return number
    }

    override fun toString(): String {
        return "$number"
    }
}

data class SnailfishNumber(
    val first: SnailfishOrRegularNumber,
    val second: SnailfishOrRegularNumber
) : SnailfishOrRegularNumber {
    override fun explode(depth: Int): ExplodeResult {
        if (depth > 4 && first is RegularNumber && second is RegularNumber) {
            return ExplodeResult(
                RegularNumber(0),
                exploded = true,
                addToLeft = first.number,
                addToRight = second.number
            )
        }
        val firstExplodeResult = first.explode(depth + 1)
        if (firstExplodeResult.exploded) {
            return ExplodeResult(
                SnailfishNumber(
                    firstExplodeResult.number,
                    if (firstExplodeResult.addToRight != null)
                        second.addToLeftmostNumber(firstExplodeResult.addToRight)
                    else second
                ),
                exploded = true,
                addToLeft = firstExplodeResult.addToLeft,
                addToRight = null
            )
        }
        val secondExplodeResult = second.explode(depth + 1)
        if (secondExplodeResult.exploded) {
            return ExplodeResult(
                SnailfishNumber(
                    if (secondExplodeResult.addToLeft != null)
                        first.addToRightmostNumber(secondExplodeResult.addToLeft)
                    else first,
                    secondExplodeResult.number
                ),
                exploded = true,
                addToLeft = null,
                addToRight = secondExplodeResult.addToRight
            )
        }
        return ExplodeResult(this, exploded = false, addToLeft = null, addToRight = null)
    }

    override fun addToLeftmostNumber(value: Int): SnailfishOrRegularNumber {
        return SnailfishNumber(first.addToLeftmostNumber(value), second)
    }

    override fun addToRightmostNumber(value: Int): SnailfishOrRegularNumber {
        return SnailfishNumber(first, second.addToRightmostNumber(value))
    }

    override fun split(): SplitResult {
        val firstSplitResult = first.split()
        if (firstSplitResult.split) {
            return SplitResult(SnailfishNumber(firstSplitResult.number, second), split = true)
        }
        val secondSplitResult = second.split()
        return SplitResult(
            SnailfishNumber(
                firstSplitResult.number, secondSplitResult.number
            ),
            secondSplitResult.split
        )
    }

    override fun magnitude(): Int {
        return first.magnitude() * 3 + second.magnitude() * 2
    }

    private fun reduce(): SnailfishNumber {
        var newNumber = this
        var tookStep: Boolean
        do {
            tookStep = false
            val explodeResult = newNumber.explode(1)
            if (explodeResult.exploded) {
                newNumber = explodeResult.number as SnailfishNumber
                tookStep = true
            } else {
                val splitResult = newNumber.split()
                if (splitResult.split) {
                    newNumber = splitResult.number as SnailfishNumber
                    tookStep = true
                }
            }
        } while (tookStep)
        return newNumber
    }

    fun add(other: SnailfishNumber): SnailfishNumber {
        return SnailfishNumber(this, other).reduce()
    }

    override fun toString(): String {
        return "[${first},${second}]"
    }
}

fun parseSnailfishOrRegularNumber(input: MutableList<Char>): SnailfishOrRegularNumber {
    val char = input[0]
    if (char.isDigit()) {
        input.removeFirst()
        return RegularNumber(char.digitToInt())
    }
    return parseSnailfishNumber(input)
}

fun parseSnailfishNumber(input: MutableList<Char>): SnailfishNumber {
    input.removeFirst()
    val first = parseSnailfishOrRegularNumber(input)
    input.removeFirst()
    val second = parseSnailfishOrRegularNumber(input)
    input.removeFirst()
    return SnailfishNumber(first, second)
}

fun main() {
    fun part1(input: List<String>): Int {
        var acc = parseSnailfishNumber(input[0].toMutableList())
        println(input[0])
        for (i in 1 until input.size) {
            println(input[i])
            acc = acc.add(parseSnailfishNumber(input[i].toMutableList()))
            println(acc)
            println()
        }
        return acc.magnitude()
    }

    fun part2(input: List<String>): Int {
        val snailfishNumbers = input.map { parseSnailfishNumber(it.toMutableList()) }
        var maxMagnitude = 0
        for (i in snailfishNumbers.indices) {
            for (j in snailfishNumbers.indices) {
                if (i != j) {
                    maxMagnitude = max(maxMagnitude, snailfishNumbers[i].add(snailfishNumbers[j]).magnitude())
                }
            }
        }
        return maxMagnitude
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    check(part2(testInput) == 3993)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}
