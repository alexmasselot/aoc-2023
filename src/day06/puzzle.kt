package day06

import println
import readInput
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sqrt


data class Boat(val time: Long, val distance: Long) {
    fun moveForTime(press: Long): Long = if (time <= press || time <= 0) {
        0
    } else {
        (time - press) * press
    }

    fun pressBeatRecord() = (1..time).filter { moveForTime(it) > distance }
    fun countPressBeatRecord(): Long {
        val p1 = (time - sqrt(1.0 * (time * time - 4 * distance))) / 2
        val p2 = (time + sqrt(1.0 * (time * time - 4 * distance))) / 2
        return (floor(p2) - ceil(p1) + 1).toLong()
    }


    companion object {
        fun readBoats(input: List<String>): List<Boat> {
            val xs = input.map { it.split(":")[1].trim() }
                .map { Regex("""\s+""").replace(it, " ") }
                .map { it.split(" ").map { it.toLong() } }
            return xs[0].zip(xs[1]).map { (t, d) -> Boat(t, d) }
        }
    }
}

fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)
    // Locate digit in a string and return the first and the last ones concatenated


    fun part1(input: List<String>): Int {
        val boats = Boat.readBoats(input)
        return boats.map {
            it.pressBeatRecord().size
        }.fold(1) { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Long {
        val boat = Boat.readBoats(input.map { it.replace(" ", "") })[0]

        return boat.countPressBeatRecord()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput)
    println(p1)
    check(part1(testInput) == 288)

//    val testInput2 = readInput("day$day/part2_test")
    val p2 = part2(testInput)
    println(p2)
    check( p2 == 71503L)

    val input = readInput("day$day/input")
    part1(input).println()
    part2(input).println()
}
