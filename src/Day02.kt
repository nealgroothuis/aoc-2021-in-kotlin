fun main() {
    fun part1(input: List<String>): Int {
        var forward = 0
        var depth = 0
        for (command in input) {
            val commandParts = command.split(" ")
            val direction = commandParts[0]
            val distance = commandParts[1].toInt()
            when (direction) {
                "forward" -> forward += distance
                "down" -> depth += distance
                "up" -> depth -= distance
            }
        }
        return forward * depth
    }

    fun part2(input: List<String>): Int {
        var forward = 0
        var depth = 0
        var aim = 0
        for (command in input) {
            val commandParts = command.split(" ")
            val direction = commandParts[0]
            val amount = commandParts[1].toInt()
            when (direction) {
                "forward" -> {
                    forward += amount
                    depth += amount * aim
                }
                "down" -> aim += amount
                "up" -> aim -= amount
            }
        }
        return forward * depth
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
