package day15

import println
import readInput

object Hash {

    fun hash(input: String) =
        input.fold(0) { acc, c -> hashChar(c, acc) }

    fun hashChar(c: Char, v: Int) =
        ((v + c.code) * 17) % 256


}

fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)

    fun part1(input: List<String>): Int {
        return input.first().split(",").sumOf{Hash.hash(it)}
    }

    fun part2(input: List<String>): Int {

        return 42
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput)
    println(p1)
    check(part1(testInput) == 1320)

//    val p2 = part2(testInput)
//    println(p2)
//    check( p2 == 281)

    val input = readInput("day$day/input")
    part1(input).println()
    part2(input).println()
}
