package day08

import println
import readInput

data class BinaryGraph(val nodes: Map<String, Int>, val lefts: List<Int>, val rights: List<Int>) {
    fun get(name: String): Int {
        return nodes[name]!!
    }

    tailrec fun walkUntil(
        target: Int,
        from: Int,
        directions: List<Int>,
        directionIndex: Int = 0,
        countSteps: Int = 0
    ): Int {
        if (target == from) {
            return countSteps
        }

        val d = directions[directionIndex]
        val next = if (d == 0) lefts[from] else rights[from]
        return walkUntil(
            target,
            next,
            directions,
            (directionIndex + 1) % directions.size,
            countSteps + 1
        )
    }

    fun countWalks(target: String, from: String, directions: String): Int {
        val directionsN = directions.map { if (it == 'L') 0 else 1 }
        return walkUntil(get(target), get(from), directionsN)
    }

    companion object {
        fun parseInput(input: List<String>): BinaryGraph {
            val nodes = input.map { it.split("=")[0].trim() }.mapIndexed { index, it -> it to index }.toMap()

            val lefts = input.map { it.split("=")[1].trim() }
                .map { it.substring(1..3) }
                .map {
                    nodes[it]!!
                }

            val rights = input.map { it.split("=")[1].trim() }
                .map { it.substring(6..8) }
                .map {
                    nodes[it]!!
                }

            return BinaryGraph(nodes, lefts, rights)
        }
    }
}


fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)

    fun part1(input: List<String>): Int {
        val directions = input[0]
        val graph = BinaryGraph.parseInput(input.drop(2))
        return graph.countWalks("ZZZ", "AAA", directions)
    }

    fun part2(input: List<String>): Int {
        val directions = input[0]
        val graph = BinaryGraph.parseInput(input.drop(2))

        val starts = graph.nodes.keys.filter { it.endsWith("A") }
        val ends = graph.nodes.keys.filter { it.endsWith("Z") }
        return 42


    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput)
    println(p1)
    check(part1(testInput) == 2)

//    val p2 = part2(testInput)
//    println(p2)
//    check( p2 == 281)

    val input = readInput("day$day/input")
    part1(input).println()
    part2(input).println()
}
