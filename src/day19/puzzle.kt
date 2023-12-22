package day19

import println
import readInput

fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)

    fun part1(input: List<String>): Int {
        val graph = Graph.parse(input.takeWhile { it != "" })
        val parts = input.dropWhile { it != "" }.map { Part.parse(it) }

        return parts.filter{graph.end(it) == "A"}.sumOf { it.sum() }

    }

    fun part2(input: List<String>): Long {
        val graph = QuantumGraph.parse(input.takeWhile { it != "" })

        return graph.endCount()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput)
    println(p1)
    check(part1(testInput) == 19114)

    val p2 = part2(testInput)
    println(p2)
    check( p2 == 167409079868000L)

    val input = readInput("day$day/input")
    part1(input).println()
    part2(input).println()
}
