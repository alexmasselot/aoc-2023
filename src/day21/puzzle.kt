package day21

import and
import mapMatrix
import or
import println
import readInput
import shiftE
import shiftN
import shiftS
import shiftW
import toStringMat
import zipApply

data class GardenMap(val gardenPlots: List<List<Boolean>>, val stepping: List<List<Boolean>>) {
    fun step(): GardenMap {
        val newStepped = stepping.shiftN(false)
            .or(stepping.shiftS(false))
            .or(stepping.shiftW(false))
            .or(stepping.shiftE(false))
            .and(gardenPlots)
        return GardenMap(gardenPlots, newStepped)
    }

    fun countStepping(): Int =
        stepping.sumOf { row -> row.count { it } }

    override fun toString() = gardenPlots.zipApply(stepping) { plot, stepped ->
        if (!plot) {
            '#'
        } else {
            if (stepped) {
                'O'
            } else {
                '.'
            }
        }
    }.toStringMat()


    companion object {
        fun parseInput(input: List<String>): GardenMap {
            val gardenPlots = input.map { it.map { it != '#' } }
            val stepped = input.map { it.map { it == 'S' } }
            return GardenMap(gardenPlots, stepped)
        }
    }
}

fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)

    fun part1(input: List<String>, steps: Int): Int {
        val gardenMap = GardenMap.parseInput(input)
        return (1..steps).fold(gardenMap){acc, _ ->
            acc.step()
        }.countStepping()
    }

    fun part2(input: List<String>): Int {

        return 42
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput, 6)
    println(p1)
    check(p1 == 16)

//    val p2 = part2(testInput)
//    println(p2)
//    check( p2 == 281)

    val input = readInput("day$day/input")
    part1(input, 64).println()
    part2(input).println()
}
