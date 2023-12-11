package day11

import findIndexes
import println
import readInput
import kotlin.math.abs

data class Universe(val grid: List<List<Boolean>>, val rowPos: List<Int>, val colPos: List<Int>) {
    fun galaxyPositions() =
        grid.findIndexes { it }.map { (row, col) -> rowPos[row].toLong() to colPos[col].toLong() }

    /**
     * double empty rows and columns
     */
    fun expand(distInc: Int): Universe {
        val emptyRows = grid.map { booleans -> booleans.all { !it } }
        val emptyCols = (0..<grid[0].size).map { col -> grid.all { !it[col] } }

        val rowIndex = shiftEmpty(emptyRows, distInc)
        val colIndex = shiftEmpty(emptyCols, distInc)

        return Universe(grid, rowIndex, colIndex)
    }

    override fun toString(): String =
        grid.joinToString("\n") { row ->
            row.joinToString("") { if (it) "#" else "." }
        }

    companion object {
        fun parseInput(input: List<String>): Universe {
            return Universe(
                input.map { it.map { it == '#' } },
                input.indices.toList(),
                (0..<input[0].length).toList()
            )
        }

        fun shiftEmpty(cond: List<Boolean>, distInc: Int): List<Int> {
            return cond.mapIndexed { index, b -> index to b }
                .fold(0 to emptyList<Int>()) { (offset, acc), (i, isEmpty) ->
                    if (isEmpty) offset + distInc-1 to acc.plus(i + offset)
                    else
                        offset to acc.plus(i + offset)
                }.second
        }

        fun distance(p1: Pair<Long, Long>, p2: Pair<Long, Long>): Long {
            return abs(p1.first - p2.first) + abs(p1.second - p2.second)
        }
    }
}

fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)

    fun sumDistance(universe: Universe): Long {
        val galaxyPositions = universe.galaxyPositions()
        return galaxyPositions.flatMap { p1 ->
            galaxyPositions.map { p2 ->
                p1 to p2
            }
        }
            .filter { it.first != it.second }
            .map { (p1, p2) ->
                Universe.distance(p1, p2)
            }
            .sum() / 2

    }

    fun part(input: List<String>, d: Int): Long {
        val universe = Universe.parseInput(input).expand(d)
        return sumDistance(universe)
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part(testInput, 2)
    println(p1)
    check(part(testInput, 2) == 374L)

    val p21 = part(testInput, 10)
    println(p21)
    check(p21 == 1030L)
    val p22 = part(testInput, 100)
    println(p22)
    check(p22 == 8410L)

    val input = readInput("day$day/input")
    part(input, 2).println()
    part(input, 1000000).println()
}
