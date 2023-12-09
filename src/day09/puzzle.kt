package day09

import println
import readInput

object Report {
    fun firstLastDeltas(input: List<Int>): List<Pair<Int, Int>> {
        tailrec fun handler(acc: List<Pair<Int, Int>>, input: List<Int>): List<Pair<Int, Int>> {
            val deltas = input.zipWithNext().map { it.second - it.first }
            if (deltas.all { it === 0 }) {
                return acc
            }
            return handler(acc.plus(deltas.first() to deltas.last()), deltas)
        }
        return handler(emptyList(), input)
    }

    fun next(input: List<Int>): Int {
        val lds = firstLastDeltas(input).map { it.second }
        return lds.reversed().plus(input.last()).fold(0) { acc, i -> acc + i }
    }

    fun previous(input: List<Int>): Int {
        val lds = firstLastDeltas(input).map { it.first }
        return lds.reversed().plus(input.first()).fold(0) { acc, i -> i - acc }
    }
}

fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)

    fun part1(input: List<String>): Int {
        return input.map { it.split(" ").map { it.toInt() } }
            .map { Report.next(it) }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return input.map { it.split(" ").map { it.toInt() } }
            .map { Report.previous(it) }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput)
    println(p1)
    check(part1(testInput) == 114)

    val p2 = part2(testInput)
    println(p2)
    check( p2 == 2)

    val input = readInput("day$day/input")
    part1(input).println()
    part2(input).println()
}
