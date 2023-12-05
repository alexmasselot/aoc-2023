package day03

import and
import mapMatrix
import mask
import or
import println
import readInput
import shiftE
import shiftN
import shiftS
import shiftW
import zipApply

fun numberSpread(input: String): List<Int?> {
    val re = Regex("[0-9]+")
    fun f(acc: List<Int?>, rem: String): List<Int?> {
        val m = re.find(rem) ?: return acc.plus(rem.map { null })
        return f(
            acc.plus((1..m.range.first).map { null })
                .plus(m.range.map { m.value.toInt() }), rem.substring(m.range.last + 1)
        )
    }
    return f(emptyList(), input)

}

fun main() {
    val day = "03"
    // Locate digit in a string and return the first and the last ones concatenated


    fun part1(input: List<String>): Int {
        val isSymbol = input.map { it.map { !(it.isDigit() || it == '.') } }
        val isSymbolNeigbor8 = isSymbol.shiftE(false)
            .or(isSymbol.shiftW(false))
            .or(isSymbol.shiftN(false))
            .or(isSymbol.shiftS(false))
            .or(isSymbol.shiftE(false).shiftN(false))
            .or(isSymbol.shiftE(false).shiftS(false))
            .or(isSymbol.shiftW(false).shiftN(false))
            .or(isSymbol.shiftW(false).shiftS(false))
            .or(isSymbol)

        val isNumber = input.map { it.map { it.isDigit() } }
        val isContaminatedNumber = (0..5).fold(isNumber.and(isSymbolNeigbor8)) { acc, _ ->
            acc.or(acc.shiftE(false)).or(acc.shiftW(false)).and(isNumber)
        }

        val contaminatedNumber = input.map { it.toList() }.mask(isContaminatedNumber, ' ')
            .map { it.joinToString("") }
        return contaminatedNumber.flatMap { Regex("""([0-9]+)""").findAll(it).map { it.value } }
            .map {
                it.toInt()
            }
            .sum()

    }

    fun part2(input: List<String>): Int {
        val numbers = input.map { l ->
            numberSpread(l)
        }.mapMatrix { it?.let { setOf(it) } ?: emptySet<Int>() }


        val numbers8 = numbers.shiftE(emptySet<Int>())
            .zipApply(numbers.shiftW(emptySet<Int>())) { a, b -> a.plus(b) }
            .zipApply(numbers.shiftN(emptySet<Int>())) { a, b -> a.plus(b) }
            .zipApply(numbers.shiftS(emptySet<Int>())) { a, b -> a.plus(b) }
            .zipApply(numbers.shiftE(emptySet<Int>()).shiftN(emptySet<Int>())) { a, b -> a.plus(b) }
            .zipApply(numbers.shiftE(emptySet<Int>()).shiftS(emptySet<Int>())) { a, b -> a.plus(b) }
            .zipApply(numbers.shiftW(emptySet<Int>()).shiftN(emptySet<Int>())) { a, b -> a.plus(b) }
            .zipApply(numbers.shiftW(emptySet<Int>()).shiftS(emptySet<Int>())) { a, b -> a.plus(b) }
            .zipApply(numbers) { a, b -> a.plus(b) }

        val isStar = input.map { it.map { it == '*' } }
        return numbers8.mask(isStar, emptySet())
            .flatten()
            .filter { it.size == 2 }
            .map { it.toList().fold(1) { acc, i -> acc * i } }
            .sum()


    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/part1_test")
    val p1 = part1(testInput)
    println(p1)
    check(p1 == 4361)

    val testInput2 = readInput("day$day/part1_test")
    val p2 = part2(testInput2)
    println(p2)
    check(p2 == 467835)

    val input = readInput("day$day/part1")
    part1(input).println()
    part2(input).println()
}
