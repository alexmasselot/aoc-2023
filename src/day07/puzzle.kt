package day07

import println
import readInput

fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)
    // Locate digit in a string and return the first and the last ones concatenated


    fun part1(input: List<String>): Int {
        val cards = input.map { Card.fromString(it) }
        return Card.sortList(cards)
            .mapIndexed { index, card ->  (index+1)*card.bid}
            .sum()
    }

    fun part2(input: List<String>): Int {
        val cards = input.map { Card2.fromString(it) }
        return Card2.sortList(cards)
            .mapIndexed { index, card ->  (index+1)*card.bid}
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput)
    println(p1)
    check(part1(testInput) == 6440)

    val p2 = part2(testInput)
    println(p2)
    check( p2 == 5905)

    val input = readInput("day$day/input")
    part1(input).println()
    part2(input).println()
}
