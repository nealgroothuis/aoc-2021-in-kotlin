interface Packet {
    val version: Long
    fun calculateValue(): Long
    fun calculateVersionSum(): Long {
        return version
    }
}

data class LiteralPacket(override val version: Long, val value: Long) : Packet {
    override fun calculateValue(): Long {
        return value
    }

    companion object {
        const val typeId = 4L
    }
}

interface OperatorPacket : Packet {
    val operands: List<Packet>
    override fun calculateVersionSum(): Long {
        return version + operands.sumOf { it.calculateVersionSum() }
    }
}

data class SumPacket(override val version: Long, override val operands: List<Packet>) : OperatorPacket {
    override fun calculateValue(): Long {
        return operands.sumOf { it.calculateValue() }
    }

    companion object {
        const val typeId = 0L
    }
}

data class ProductPacket(override val version: Long, override val operands: List<Packet>) : OperatorPacket {
    override fun calculateValue(): Long {
        return operands.map { it.calculateValue() }.reduce { i, j -> i * j }
    }

    companion object {
        const val typeId = 1L
    }
}

data class MinimumPacket(override val version: Long, override val operands: List<Packet>) : OperatorPacket {
    override fun calculateValue(): Long {
        return operands.map { it.calculateValue() }.minOf { it }
    }

    companion object {
        const val typeId = 2L
    }
}


data class MaximumPacket(override val version: Long, override val operands: List<Packet>) : OperatorPacket {
    override fun calculateValue(): Long {
        return operands.map { it.calculateValue() }.maxOf { it }
    }

    companion object {
        const val typeId = 3L
    }
}

data class GreaterThanPacket(override val version: Long, override val operands: List<Packet>) : OperatorPacket {
    override fun calculateValue(): Long {
        return if (operands[0].calculateValue() > operands[1].calculateValue()) 1 else 0
    }

    companion object {
        const val typeId = 5L
    }
}

data class LessThanPacket(override val version: Long, override val operands: List<Packet>) : OperatorPacket {
    override fun calculateValue(): Long {
        return if (operands[0].calculateValue() < operands[1].calculateValue()) 1 else 0
    }

    companion object {
        const val typeId = 6L
    }
}

data class EqualToPacket(override val version: Long, override val operands: List<Packet>) : OperatorPacket {
    override fun calculateValue(): Long {
        return if (operands[0].calculateValue() == operands[1].calculateValue()) 1 else 0
    }

    companion object {
        const val typeId = 7L
    }
}

fun bitsToLong(bitStream: List<Int>): Long {
    var acc = 0L
    for (bit in bitStream) {
        acc = acc.shl(1) + bit
    }
    return acc
}

fun removeNBits(n: Long, bitStream: MutableList<Int>): List<Int> {
    val newList = mutableListOf<Int>()
    for (i in 1..n) {
        newList.add(bitStream.removeFirst())
    }
    return newList
}

fun getLiteralValue(bitStream: MutableList<Int>): Long {
    val literalValueBits = mutableListOf<Int>()
    var fiveBits: List<Int>?
    do {
        fiveBits = removeNBits(5, bitStream)
        for (i in 1..4) {
            literalValueBits.add(fiveBits[i])
        }
    } while (fiveBits!![0] == 1)
    return bitsToLong(literalValueBits)
}

fun parsePacket(bitStream: MutableList<Int>): Packet {
    val version = bitsToLong(removeNBits(3, bitStream))
    val typeId = bitsToLong(removeNBits(3, bitStream))
    if (typeId == LiteralPacket.typeId) {
        val value = getLiteralValue(bitStream)
        return LiteralPacket(version, value)
    }
    val lengthTypeId = removeNBits(1, bitStream)[0]
    val operands = mutableListOf<Packet>()
    if (lengthTypeId == 0) {
        val operandsLengthInBits = bitsToLong(removeNBits(15, bitStream))
        val operandBits = removeNBits(operandsLengthInBits, bitStream).toMutableList()
        while (operandBits.isNotEmpty()) {
            operands.add(parsePacket(operandBits))
        }
    } else {
        val numOperands = bitsToLong(removeNBits(11, bitStream))
        for (i in 1..numOperands) {
            operands.add(parsePacket(bitStream))
        }
    }
    return when (typeId) {
        SumPacket.typeId -> SumPacket(version, operands)
        ProductPacket.typeId -> ProductPacket(version, operands)
        MinimumPacket.typeId -> MinimumPacket(version, operands)
        MaximumPacket.typeId -> MaximumPacket(version, operands)
        GreaterThanPacket.typeId -> GreaterThanPacket(version, operands)
        LessThanPacket.typeId -> LessThanPacket(version, operands)
        EqualToPacket.typeId -> EqualToPacket(version, operands)
        else -> throw RuntimeException("Invalid packet type ID $typeId")
    }
}

fun createBitStream(input: List<String>): MutableList<Int> {
    val values = input[0].map { it.digitToInt(16) }
    return values.flatMap {
        val bits = mutableListOf<Int>()
        var v = it
        for (i in 1..4) {
            v = v.shl(1)
            bits.add(if (v.and(0x10) == 0) 0 else 1)
        }
        bits
    }.toMutableList()
}

fun main() {
    fun part1(input: List<String>): Long {
        return parsePacket(createBitStream(input)).calculateVersionSum()
    }

    fun part2(input: List<String>): Long {
        return parsePacket(createBitStream(input)).calculateValue()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part2(testInput) == 3L)

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}
