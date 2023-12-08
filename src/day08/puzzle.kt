package day08

import println
import readInput
import kotlin.math.abs
import kotlin.math.sqrt

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
        if (target == from && countSteps > 0) {
            return countSteps
        }
        if (countSteps > nodes.size * nodes.size) {
            return -1
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

            val reLeftRight = """.*\(([A-Z]{3}), ([A-Z]{3})\)""".toRegex()
            val lrs = input.map{
                val (l, r) = reLeftRight.find(it)!!.destructured
                l to r
            }
            val lefts = lrs.map{nodes[it.first]!!}
            val rights = lrs.map{nodes[it.second]!!}

            return BinaryGraph(nodes, lefts, rights)
        }

        fun findLCM(numbers: List<Long>): Long {
            require(numbers.isNotEmpty()) { "List must not be empty" }

            fun gcd(a: Long, b: Long): Long {
                return if (b == 0L) abs(a) else gcd(b, a % b)
            }

            fun lcm(a: Long, b: Long): Long {
                return if (a == 0L || b == 0L) 0 else abs(a * b) / gcd(a, b)
            }

            return numbers.reduce { acc, num -> lcm(acc, num) }
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

    fun part2(input: List<String>): Long {
        val directions = input[0]
        val graph = BinaryGraph.parseInput(input.drop(2))

        val starts = graph.nodes.keys.filter { it.endsWith("A") }
        val ends = graph.nodes.keys.filter { it.endsWith("Z") }

        /**
         * There are conditions on the entry set
         * each of the i .*A goes to one *.Z n(i)
         * each .*Z goes only to itself with m(i)=n(i)
         * WE just need then to dinf the lowest common multiplier
         */

        val ns = starts.map { start ->
            val e = ends.first() { end ->
                val count = graph.countWalks(end, start, directions)
                count > 0
            }
            val n = graph.countWalks(e, start, directions)
            val m = graph.countWalks(e, e, directions)
            println("$start\t$e\t$n\t$m")
            check(m == n)
            n.toLong()
        }
        return BinaryGraph.findLCM(ns)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput)
    check(part1(testInput) == 2)

//    val p2 = part2(testInput)
//    println(p2)
//    check( p2 == 281)

    val input = readInput("day$day/input")
    part1(input).println()
    part2(input).println()
}
