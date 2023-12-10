package day10

import println
import readInput

data class Mover(val position: List<Int>, val direction: List<Int>) {
    fun step(pipeUnit: PipeUnit): Mover {
        val d = pipeUnit.steer(direction)
        return Mover(listOf(position[0] + d[0], position[1] + d[1]), d)
    }

}

data class PipeUnit(val matrix: List<List<Int>>, val possible: Set<List<Int>>) {
    fun steer(direction: List<Int>) =
        listOf(
            direction[0] * matrix[0][0] + direction[1] * matrix[0][1],
            direction[0] * matrix[1][0] + direction[1] * matrix[1][1]
        )


    companion object {
        val letterSteps = mapOf(
            "|" to (PipeUnit(listOf(listOf(1, 0), listOf(0, 1)), setOf(listOf(0, 1), listOf(0, -1)))),
            "-" to (PipeUnit(listOf(listOf(1, 0), listOf(0, 1)), setOf(listOf(1, 0), listOf(-1, 0)))),
            "L" to (PipeUnit(listOf(listOf(0, 1), listOf(1, 0)), setOf(listOf(0, 1), listOf(-1, 0)))),
            "7" to (PipeUnit(listOf(listOf(0, 1), listOf(1, 0)), setOf(listOf(1, 0), listOf(0, 1)))),
            "F" to (PipeUnit(listOf(listOf(0, -1), listOf(-1, 0)), setOf(listOf(-1, 0), listOf(0, -1)))),
            "J" to (PipeUnit(listOf(listOf(0, -1), listOf(-1, 0)), setOf(listOf(0, 1), listOf(1, 0)))),
        )
    }
}

data class PipeWorld(val start: List<Int>, val pipes: List<List<PipeUnit?>>) {
    fun possibleStarts(): List<Mover> =
        listOf(
            listOf(0, 1),
            listOf(0, -1),
            listOf(1, 0),
            listOf(-1, 0),
        ).filter {
            start[0] + it[0] >= 0 && start[1] + it[1] >= 0
        }
            .filter {
                pipes[start[0] + it[0]][start[1] + it[1]]?.possible?.contains(it) ?: false
            }.map {
                Mover(
                    listOf(start[0] + it[0], start[1] + it[1]),
                    it
                )
            }

    fun step(mover: Mover) =
        mover.step(pipes[mover.position[0]][mover.position[1]]!!)

    fun furthestPath(): Int {
        val starts = possibleStarts()
        fun handler(i: Int, m1: Mover, m2: Mover): Int {
            if (m1.position == m2.position && i > 0) {
                return i
            }
            return handler(i + 1, step(m1), step(m2))
        }

        return handler(1, starts[0], starts[1])
    }


    companion object {
        /** transpose a list of lists */
        fun <T> List<List<T>>.transpose(): List<List<T>> =
            if (isEmpty()) listOf()
            else (this[0].indices).map { col ->
                map { row -> row[col] }
            }

        fun fromInput(input: List<String>): PipeWorld {
            val inputTR = input.map { it.toList() }.transpose()

            val pipes = inputTR.map {
                it.map { PipeUnit.letterSteps[it.toString()] }
            }

            val start = input.withIndex().first { (_, s) -> s.contains("S") }
                .let { (i, s) -> listOf(s.indexOf('S'), i) }
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
    part2(input).println()
}
