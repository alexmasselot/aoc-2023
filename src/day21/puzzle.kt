package day21

import and
import countIf
import emptyMat
import mapMatrix
import or
import println
import readInput
import set
import shiftE
import shiftN
import shiftS
import shiftW
import toString
import toStringMat
import toStringPad
import zipApply

data class GardenMap(val gardenPlots: List<List<Boolean>>, val stepping: List<List<Boolean>>) {
    val mid = (gardenPlots.size - 1) / 2
    fun step(): GardenMap {
        val newStepped =
            stepping.shiftN(false).or(stepping.shiftS(false)).or(stepping.shiftW(false)).or(stepping.shiftE(false))
                .and(gardenPlots)
        return GardenMap(gardenPlots, newStepped)
    }


    fun countStepping(): Int = stepping.sumOf { row -> row.count { it } }

    fun init(row: Int, col: Int) = this.copy(stepping = stepping.set(row, col, true))

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
            val stepped = emptyMat(input.size, input.size, false)
            return GardenMap(gardenPlots, stepped)
        }
    }
}

fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)

    fun part1(input: List<String>, steps: Int): Int {
        val gardenMap = GardenMap.parseInput(input)
        println("mid: ${gardenMap.mid}")
        return (1..steps).fold(gardenMap.init(gardenMap.mid, gardenMap.mid)) { acc, i ->
//            println(acc)
//            println("$i\t${acc.countStepping()}")
            acc.step()
        }.countStepping()
    }

    fun growCount(input: List<String>, startRow: Int, startCol: Int, maxSteps: Int): List<Int> {
        val gardenMap = GardenMap.parseInput(input).init(startRow, startCol)

        return (1..maxSteps).scan(gardenMap) { acc, i ->
            //println(acc)
            //println("$i\t${acc.countStepping()}")
            acc.step()
        }.map { it.countStepping() }
    }

    fun grow(garden: GardenMap, maxStep: Int): GardenMap {
        return (1..maxStep).fold(garden) { acc, _ ->
            acc.step()
        }
    }

    fun part2(input: List<String>): Long {
        val nbRepeat = 4
        val inputN = (0..(2 * nbRepeat)).flatMap { input.map { row -> row.repeat(2 * nbRepeat + 1) } }
        val mid = nbRepeat * input.size + (input.size - 1) / 2
        val grown = grow(GardenMap.parseInput(inputN).init(mid, mid), mid)

        println("total: ${grown.countStepping()}")
        val blockCount = (0..(2 * nbRepeat)).map { bRow ->
            (0..(2 * nbRepeat)).map { bCol ->
                val block = grown.stepping.subList(bRow * input.size, (bRow + 1) * input.size)
                    .map { it.subList(bCol * input.size, (bCol + 1) * input.size) }
                block.countIf { it }
            }
        }
        println(blockCount.toStringPad())
        fun extrapolateStepping(nbSteps: Int): Long {
            val r = (nbSteps - (input.size - 1) / 2) / input.size

            println("nbSteps: $nbSteps, r: $r")
            return r.toLong() * r * blockCount[1][nbRepeat] +
                    (r - 1) * (r - 1) * blockCount[nbRepeat][nbRepeat] +
                    (r - 1) * (blockCount[1][nbRepeat - 1] + blockCount[1][nbRepeat + 1] + blockCount[2 * nbRepeat - 1][nbRepeat - 1] + blockCount[2 * nbRepeat - 1][nbRepeat + 1]) +
                    r * (blockCount[0][nbRepeat - 1] + blockCount[0][nbRepeat + 1] + blockCount[2 * nbRepeat][nbRepeat - 1] + blockCount[2 * nbRepeat][nbRepeat + 1]) +
                    blockCount[0][nbRepeat] + blockCount[2 * nbRepeat][nbRepeat] + blockCount[nbRepeat][0] + blockCount[nbRepeat][2 * nbRepeat]
        }

        println("${grown.countStepping()} / ${extrapolateStepping(mid)}")
        check(grown.countStepping().toLong() == extrapolateStepping(mid))

        return extrapolateStepping(26501365)
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
