package day10

import println
import readInput

data class Mover(val position: List<Int>, val direction: List<Int>) {
    fun step(pipeUnit: PipeUnit): Mover {
        val d = pipeUnit.steer(direction)
        return Mover(listOf(position[0] + d[0], position[1] + d[1]), d)
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

    fun <T> walkTheFence(init: T, f: (T, Mover) -> T): T {
        tailrec fun handler(acc: T, m: Mover): T {
            val next = f(acc, m)
            if (m.position == start) {
                return acc
            }
            return handler(f(acc, m), step(m))
        }

        return handler(init, possibleStarts().first())
    }

    fun furthestPath(): Int {
        val n = walkTheFence(0) { i: Int, _: Mover -> i + 1 }
        return n / 2 + n % 2

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

        return 42
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
