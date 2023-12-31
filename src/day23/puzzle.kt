package day23

import day23.explo.main
import println
import readInput

fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)

    fun part1(input: List<String>): Int {
        val tileMap = TileMap.parse(input)
        val graphMap = GraphMap.build(tileMap)

        val dGraph = graphMap.bifurcationsGraph()
        return dGraph.findLongestPathDistance(graphMap.startPos, graphMap.endPos)
    }

    fun part2(input: List<String>): Int {
        val tileMap = TileMap.parse(input.map { it.replace("""[<>v^]""".toRegex(), ".") })
        val graphMap = GraphMap.build(tileMap)

        val dGraph = graphMap.bifurcationsGraph()
        return dGraph.findLongestPathDistance(graphMap.startPos, graphMap.endPos)

    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput)
    println(p1)
    check(part1(testInput) == 94)

    val p2 = part2(testInput)
    println(p2)
    check( p2 == 154)

    val input = readInput("day$day/input")
    part1(input).println()
    part2(input).println()
}
