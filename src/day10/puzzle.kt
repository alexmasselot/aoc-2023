package day10

import and
import countIf
import emptyMat
import mapMatrix
import mask
import nbCols
import nbRows
import not
import or
import println
import readInput
import set
import shiftE
import shiftN
import shiftS
import shiftW
import toStringMat
import zipApply

data class Mover(val position: List<Int>, val direction: List<Int>) {
    fun step(pipeUnit: PipeUnit): Mover {
        val d = pipeUnit.steer(direction)
        return Mover(listOf(position[0] + d[0], position[1] + d[1]), d)
    }

    /**
     * -1 => left
     * 0 => straight
     * 1 => right
     */
    fun turning(pipeUnit: PipeUnit): Int {
        val newDir = pipeUnit.steer(direction)
        return newDir[0] * direction[1] - newDir[1] * direction[0]
    }
}

data class PipeUnit(val symbol: Char, val matrix: List<List<Int>>, val possible: Set<List<Int>>) {
    fun steer(direction: List<Int>) = listOf(
        direction[0] * matrix[0][0] + direction[1] * matrix[0][1],
        direction[0] * matrix[1][0] + direction[1] * matrix[1][1]
    )


    companion object {
        val letterSteps = mapOf(
            '|' to (PipeUnit('|', listOf(listOf(1, 0), listOf(0, 1)), setOf(listOf(1, 0), listOf(-1, 0)))),
            '-' to (PipeUnit('-', listOf(listOf(1, 0), listOf(0, 1)), setOf(listOf(0, 1), listOf(0, -1)))),
            'L' to (PipeUnit('L', listOf(listOf(0, 1), listOf(1, 0)), setOf(listOf(1, 0), listOf(0, -1)))),
            '7' to (PipeUnit('7', listOf(listOf(0, 1), listOf(1, 0)), setOf(listOf(0, 1), listOf(1, 0)))),
            'F' to (PipeUnit('F', listOf(listOf(0, -1), listOf(-1, 0)), setOf(listOf(0, -1), listOf(-1, 0)))),
            'J' to (PipeUnit('J', listOf(listOf(0, -1), listOf(-1, 0)), setOf(listOf(1, 0), listOf(0, 1)))),
        )
    }
}

data class PipeWorld(val start: List<Int>, val pipes: List<List<PipeUnit?>>) {
    fun possibleStarts(): List<Mover> = listOf(
        listOf(0, 1),
        listOf(0, -1),
        listOf(1, 0),
        listOf(-1, 0),
    ).filter {
        start[0] + it[0] >= 0 && start[1] + it[1] >= 0
    }.filter {
        pipes[start[0] + it[0]][start[1] + it[1]]?.possible?.contains(it) ?: false
    }.map {
        Mover(
            listOf(start[0] + it[0], start[1] + it[1]), it
        )
    }

    fun step(mover: Mover) = mover.step(pipes[mover.position[0]][mover.position[1]]!!)

    private fun <T> walkTheFence(init: T, reverse: Boolean = false, f: (T, Mover) -> T): T {
        tailrec fun handler(acc: T, m: Mover): T {
            if (m.position == start) {
                return acc
            }
            return handler(f(acc, m), step(m))
        }

        val ps = if (reverse) possibleStarts()[1] else possibleStarts()[0]
        return handler(init, ps)
    }

    fun furthestPath(): Int {
        val n = walkTheFence(0) { i: Int, _: Mover -> i + 1 }
        return n / 2 + n % 2
    }

    fun turningTotal(reverse: Boolean = false): Int {
        return walkTheFence(0, reverse) { i: Int, m: Mover -> i + m.turning(pipes[m.position[0]][m.position[1]]!!) }
    }

    fun fenceMap(): List<List<Boolean>> {
        val init = emptyMat(pipes.size, pipes[0].size, false).set(start[0], start[1], true)
        return walkTheFence(init) { acc: List<List<Boolean>>, m: Mover -> acc.set(m.position[0], m.position[1], true) }
    }

    fun paintInside(): List<List<Boolean>> {
        val fence = fenceMap()
        val isTurningRight = turningTotal() > 0

        val init = emptyMat(pipes.size, pipes[0].size, false)
        val insideAgainstBorder =  walkTheFence(init, !isTurningRight) { acc: List<List<Boolean>>, m: Mover ->
            val pipe = pipes[m.position[0]][m.position[1]]!!


            listOf(m.direction, pipe.steer(m.direction)).map { dir ->
                val rightDir = listOf(
                    dir[1],
                    -dir[0]
                )
                listOf(m.position[0] + rightDir[0], m.position[1] + rightDir[1])
            }
                .filter { pos ->
                    pos[0] >= 0
                            && pos[1] >= 0
                            && pos[0] < pipes.nbRows()
                            && pos[1] < pipes.nbCols()
                            && !fence[pos[0]][pos[1]]
                }
                .fold(acc) { acc, pos ->
                    acc.set(pos[0], pos[1], true)
                }
        }

        tailrec fun growInside(inside: List<List<Boolean>>): List<List<Boolean>> {
            val newInside = inside.or(inside.shiftW(false))
                .or(inside.shiftE(false))
                .or(inside.shiftN(false))
                .or(inside.shiftS(false))
                .and(fence.not())
            if(newInside == inside) {
                return inside
            }
            return growInside(newInside)
        }
        return growInside(insideAgainstBorder)
    }


    companion object {
        fun fromInput(input: List<String>): PipeWorld {
            val inputTR = input.map { it.toList() }

            val pipes = inputTR.map {
                it.map { PipeUnit.letterSteps[it] }
            }

            val start =
                input.withIndex().first { (_, s) -> s.contains("S") }.let { (i, s) -> listOf(i, s.indexOf('S')) }
            return PipeWorld(start, pipes)
        }
    }
}

/**
an enum to associate a letter with a object of the class Stepper
 *
 */


fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)

    fun part1(input: List<String>): Int {
        val pw = PipeWorld.fromInput(input)
        return pw.furthestPath()
    }

    fun part2(input: List<String>): Int {
        val pw = PipeWorld.fromInput(input)
        val gotMap = pw.pipes.mapMatrix { it?.symbol ?: '.' }
            .set(pw.start[0], pw.start[1], 'S')
            .zipApply(pw.paintInside()) { m, inside ->
                if (inside) 'I' else m
            }
            .toStringMat()
        println(gotMap)

        return pw.paintInside().countIf{ it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput)
    println(p1)
    check(part1(testInput) == 8)

//    val p2 = part2(testInput)
//    println(p2)
//    check( p2 == 281)

    val input = readInput("day$day/input")
    part1(input).println()
    check(part1(input) == 6717)
    part2(input).println()
}
