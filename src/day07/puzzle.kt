package day07

import println
import readInput

fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)
    // Locate digit in a string and return the first and the last ones concatenated


    fun part1(input: List<String>): Int {
        return 42
    }

    fun part2(input: List<String>): Int {

        return 42
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput)
    println(p1)
    check(part1(testInput) == 42)

//    val testInput2 = readInput("day$day/part2_test")
//    val p2 = part2(testInput)
//    println(p2)
//    check( p2 == 281)

    val input = readInput("day$day/input")
    part1(input).println()
    part2(input).println()
}
