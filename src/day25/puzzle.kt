package day25

import println
import readInput
import java.util.BitSet

data class Graph(val connections: Map<String, Set<String>>) {
    fun countTwoPartSizes(): Pair<Int, Int> {
        fun handler(acc: Set<String>, remaining: Set<String>): Set<String> {
            if (remaining.isEmpty()) return acc
            val next = remaining.first()
            return handler(
                acc.plus(next),
                remaining.minus(next).plus(connections[next]!!.filterNot { acc.contains(it) })
            )
        }

        val nTot = connections.keys.size
        val root = connections.keys.first()
        return handler(setOf(root), connections[root]!!).let { it.size to nTot - it.size }
    }

    companion object {
        fun parse(input: List<String>): Graph {
            val nodesIndex = input.flatMap { it.split(":? ".toRegex()) }
                .distinct()
                .mapIndexed { index, s -> s to index }
                .toMap()
            val n = nodesIndex.size
            val connections = input.flatMap { l ->
                val (src, tos) = l.split(": ".toRegex())
                val connectionsIndex = tos.split(" ")
                connectionsIndex.map { it to src }.plus(connectionsIndex.map { src to it })
            }
                .distinct()
                .groupBy { it.first }
                .map { it.key to it.value.map { it.second }.toSet() }
                .toMap()

            connections.forEach {
                println("${it.key}\t${it.value}")
            }

            return Graph(connections)
        }
    }
}

fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)

    fun part1(input: List<String>): Int {
        val graph = Graph.parse(input)
        val (n1, n2) = graph.countTwoPartSizes()
        println("$n1, $n2")
        return n1 * n2
    }

    fun part2(input: List<String>): Int {

        return 42
    }

    // test if implementation meets criteria from the description, like:

//    val p2 = part2(testInput)
//    println(p2)
//    check( p2 == 281)

    val input = readInput("day$day/input-no-bridges")
    part1(input).println()
    part2(input).println()
}
