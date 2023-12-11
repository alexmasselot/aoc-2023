package day11

import findIndexes
import println
import readInput

data class Universe(val grid: List<List<Boolean>>) {
    fun galaxyPositions()=
        grid.findIndexes { it }

    /**
     * double empty rows and columns
     */
    fun expand(): Universe {
        val emptyRowDeltas = listOf(0).plus(grid.mapIndexed { index, booleans -> index to booleans.all { !it } }
            .filter { it.second }
            .map { it.first }
        )
            .zipWithNext()

        val emptyColDeltas = listOf(0).plus((0..<grid[0].size).filter { col -> grid.all { !it[col] } })
            .zipWithNext()

        val withRows = emptyRowDeltas.fold(emptyList<List<Boolean>>()) { acc, delta ->
            acc.plus(grid.drop(delta.first).take(delta.second-delta.first+1))
        }.plus(grid.drop(emptyRowDeltas.last().second))

        val withCols= withRows.map{row ->
            emptyColDeltas.fold(emptyList<Boolean>()) { acc, delta ->
                acc.plus(row.drop(delta.first).take(delta.second-delta.first+1))
            }.plus(row.drop(emptyColDeltas.last().second))
        }

        return Universe(withCols)
    }

    override fun toString(): String =
        grid.joinToString("\n") { row ->
            row.joinToString("") { if (it) "#" else "." }
        }

    companion object {
        fun parseInput(input: List<String>): Universe {
            return Universe(input.map { it.map { it == '#' } })
        }

        fun distance(p1: Pair<Int, Int>, p2: Pair<Int, Int>): Int {
             return Math.abs(p1.first - p2.first) + Math.abs(p1.second - p2.second)
        }
    }
}

fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)

    fun part1(input: List<String>): Int {
        val universe = Universe.parseInput(input).expand()
        val galaxyPositions = universe.galaxyPositions()
        return galaxyPositions.flatMap { p1 ->
            galaxyPositions.map { p2 ->
                p1 to p2
            }
        }
            .filter { it.first != it.second }
            .map{ (p1, p2) -> Universe.distance(p1, p2)
            }
            .sum()/2


    }

    fun part2(input: List<String>): Int {

        return 42
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput)
    println(p1)
    check(part1(testInput) == 374)

//    val p2 = part2(testInput)
//    println(p2)
//    check( p2 == 281)

    val input = readInput("day$day/input")
    part1(input).println()
    part2(input).println()
}
