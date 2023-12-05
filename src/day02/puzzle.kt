package day02

import println
import readInput
import kotlin.math.max

val col_idx = mapOf(
    "blue" to 0,
    "red" to 1,
    "green" to 2,
    "yellow" to 3
)
val cols = listOf("blue", "red", "green", "yellow")

fun readOneDraw(input: String): List<Int> {
    return cols.map { c ->
        val regex = Regex("([0-9]+) $c")
        val match = regex.find(input)
        if (match != null) {
            match.groupValues[1].toInt()
        } else {
            0
        }
    }
}

/**
 * Read a game line Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
 * into [[3,4,0,0], [6,0,2,0], [0,0,2,0]]
 */
fun readOneGame(input: String): List<List<Int>> {
    return input.split(":")[1]
        .split(";")
        .map { readOneDraw(it) }

}

fun main() {
    val day = "02"


    fun part1(input: List<String>): Int {
        val target = listOf(14, 12, 13, 0)
        return input.mapIndexed { index, g ->
            index to readOneGame(g)
        }.filter { (_, g) ->
            g.all({ it.zip(target).all { (a, b) -> a <= b } })
        }.map { (idx, _) -> idx + 1 }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return input.mapIndexed { index, g ->
            index to readOneGame(g)
        }.map { (_, g) ->
            g.fold(listOf(0, 0, 0, 0)) { acc, draw ->
                val r = acc.zip(draw).map { (a, b) -> max(a, b) }
                r
            }
                .take(3)
                .fold(1) { acc, i -> acc * i }
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/part1")
    val p1 = part1(testInput)
    println(p1)
//    check(part1(testInput) == 42)

    val testInput2 = readInput("day$day/part1_test")
    val p2 = part2(testInput2)
    println(p2)
    check(p2 == 2286)

    val input = readInput("day$day/part1")
    part1(input).println()
    part2(input).println()
}
