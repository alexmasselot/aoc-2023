package day17

import mapMatrix
import println
import readInput
import kotlin.math.min

data class HeatNode(
    val pos: Pair<Int, Int>,
    val comingFrom: Int,
    val remainingSteps: Int
)

/**
 * Directions
 * - 0: north
 * - 1: west
 * - 2: south
 * - 3: east
 */
class HeatMap(val values: Array<Array<Int>>) {
    val size = values.size


    init {
        check(values.size == values[0].size)
    }

    /**
     * row, column, direction walking here, remaining possible steps
     */

    fun findShortestDistance(): Int {
        val shortestPath = (0..3).flatMap { dir ->
            (0..2).map { rem ->
                HeatNode(0 to 0, dir, rem) to 0
            }
        }
            .toMap()
            .toMutableMap()

        val neighborsPosition = listOf(
            { p: Pair<Int, Int> -> p.first - 1 to p.second },
            { p: Pair<Int, Int> -> p.first to p.second - 1 },
            { p: Pair<Int, Int> -> p.first + 1 to p.second },
            { p: Pair<Int, Int> -> p.first to p.second + 1 },
        )
        val fifo = mutableListOf<HeatNode>()
        shortestPath.keys.forEach { fifo.add(it) }

        fun handler(node: HeatNode) {
            (0..3).forEach { newDir ->
                val newPos = neighborsPosition[newDir](node.pos)
                if (newPos.first < 0 || newPos.second < 0 || newPos.first >= size || newPos.second >= size) {
                    return@forEach
                }
                if (newDir == node.comingFrom && node.remainingSteps == 0) {
                    return@forEach
                }
                if (newDir == (node.comingFrom + 2) % 4) {
                    return@forEach
                }
                val currentShortest = shortestPath[node]!!
                val currentHeat = values[newPos.first][newPos.second]

                val newRem = if (newDir == node.comingFrom) node.remainingSteps - 1 else 2
                val newNode = HeatNode(newPos, newDir, newRem)
                val newShortest = shortestPath[newNode]

                // println("$p -> $newPos = $currentShortest + $currentHeat")
                if (newShortest == null || newShortest!! > currentShortest + currentHeat) {
                    shortestPath[newNode] = currentShortest + currentHeat
                    fifo.add(newNode)
                }
            }
        }
        while (fifo.isNotEmpty()) {
            val first = fifo.removeFirst()
            handler(first)
        }

        val shortestMap = Array(size) { Array(size) { Integer.MAX_VALUE } }
        shortestPath.forEach {
            val r = it.key.pos.first
            val c = it.key.pos.second
            shortestMap[r][c] = min(shortestMap[r][c], it.value)
        }

        return shortestPath.filter { it.key.pos == size - 1 to size - 1 }.map { it.value }.min()
    }
}

fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)

    fun part1(input: List<String>): Int {
        val hm = HeatMap(input.map { l ->
            l.toList().map { c ->
                c.toString().toInt()
            }.toTypedArray()
        }.toTypedArray())
        return hm.findShortestDistance()
    }

    fun part2(input: List<String>): Int {

        return 42
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput)
    println(p1)
    check(p1 == 102)

//    val p2 = part2(testInput)
//    println(p2)
//    check( p2 == 281)

    val input = readInput("day$day/input")
    part1(input).println()
    part2(input).println()
}
