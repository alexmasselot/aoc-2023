package day19

import println
import readInput

data class Graph(val nodes: Map<String, Node>) {
    fun end(p: Part): String {
        tailrec fun handler(nodeName: String): String {
            val next = nodes[nodeName]!!.next(p)
            if (next == "A" || next == "R") return next
            return handler(next)
        }
        return handler("in")
    }

    companion object {
        fun parse(input: List<String>): Graph {
            val nodes = input.map { Node.parse(it) }.map { it.name to it }.toMap()
            return Graph(nodes)
        }
    }
}

data class Part(val x: Int = 0, val m: Int = 0, val a: Int = 0, val s: Int = 0) {
    fun sum() = x + m + a + s

    companion object {
        fun parse(input: String): Part {
            val re = """([xmas])=(\d+)""".toRegex()
            return re.findAll(input).fold(Part()) { acc, m ->
                val (x, v) = m.destructured
                when (x) {
                    "x" -> acc.copy(x = v.toInt())
                    "m" -> acc.copy(m = v.toInt())
                    "a" -> acc.copy(a = v.toInt())
                    "s" -> acc.copy(s = v.toInt())
                    else -> throw Exception("Unknown field $x")
                }
            }
        }
    }
}

typealias Brancher = (Part) -> String?

data class Node(val name: String, val branches: List<Brancher>) {
    fun next(p: Part): String =
        branches.firstNotNullOfOrNull { it(p) } ?: throw Exception("No branch found for $p")

    companion object {
        fun parseBrancher(input: String): Brancher {
            val reLT = """([xmas])<(\d+):(\w+)""".toRegex()
            val reGT = """([xmas])>(\d+):(\w+)""".toRegex()
            val reEQ = """(\w+)""".toRegex()
            if (reLT.matches(input)) {
                val (x, vs, next) = reLT.matchEntire(input)!!.destructured
                val v = vs.toInt()
                return when (x) {
                    "x" -> { p -> if (p.x < v) next else null }
                    "m" -> { p -> if (p.m < v) next else null }
                    "a" -> { p -> if (p.a < v) next else null }
                    "s" -> { p -> if (p.s < v) next else null }
                    else -> throw Exception("Unknown field $x")
                }
            }
            if (reGT.matches(input)) {
                val (x, vs, next) = reGT.matchEntire(input)!!.destructured
                val v = vs.toInt()
                return when (x) {
                    "x" -> { p -> if (p.x > v) next else null }
                    "m" -> { p -> if (p.m > v) next else null }
                    "a" -> { p -> if (p.a > v) next else null }
                    "s" -> { p -> if (p.s > v) next else null }
                    else -> throw Exception("Unknown field $x")
                }
            }
            if (reEQ.matches(input)) {
                return { _ -> input }
            }
            throw Exception("cannot parse function $input")

        }

        fun parse(input: String): Node {
            val re = """(\w+)\{(.*)\}""".toRegex()
            val (name, brs) = re.matchEntire(input)!!.destructured
            return Node(name, brs.split(",").map { parseBrancher(it) })
        }

    }
}

fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)

    fun part1(input: List<String>): Int {
        val graph = Graph.parse(input.takeWhile { it != "" })
        val parts = input.dropWhile { it != "" }.map { Part.parse(it) }

        return parts.filter{graph.end(it) == "A"}.sumOf { it.sum() }

    }

    fun part2(input: List<String>): Int {

        return 42
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput)
    println(p1)
    check(part1(testInput) == 19114)

//    val p2 = part2(testInput)
//    println(p2)
//    check( p2 == 281)

    val input = readInput("day$day/input")
    part1(input).println()
    part2(input).println()
}
