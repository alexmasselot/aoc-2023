package day13

import println
import readInput
import kotlin.math.min

data class Pattern(val map: List<Int>, val nbCols: Int, val trs: Pattern? = null) {
    val nbRows = map.size
    val transposed = trs ?: transpose()
    fun transpose() =
        Pattern((0..<nbRows)
            .fold(List(nbCols) { 0 }) { acc, row ->
                acc.mapIndexed { i, v -> v * 2 + ((map[row] shr i) and 1) }
            }.reversed(), nbRows, this
        )

    fun findAllMirrorRows(nbSmudges: Int = 0): List<Int> =
        (0..nbRows).filter { isRowMirrored(it, nbSmudges) }

    fun findAllMirrorCols(nbSmudges: Int = 0): List<Int> =
        (0..nbCols).filter { isColMirrored(it, nbSmudges) }

    fun isRowMirrored(row: Int, nbSmudges: Int = 0): Boolean {
        println("---------- $row")
        if (row == 0 || row >= nbRows) {
            return false
        }
        val iMax = min(row, nbRows - row)
        if ((1..iMax).any {
            println("$it\t${binaryAdd(map[row - it], map[row + it - 1], nbSmudges)}")
                binaryAdd(map[row - it], map[row + it - 1], nbSmudges) == null
            }) {
            return false
        }
        return (1..iMax).sumOf {
            binaryAdd(map[row - it], map[row + it - 1], nbSmudges)!!
        } == nbSmudges
    }

    fun isColMirrored(col: Int, nbSmudges: Int = 0): Boolean = transposed.isRowMirrored(col, nbSmudges)

    override fun toString(): String =
        map.map { n ->
            (0 until nbCols).reversed().map { i ->
                if ((n shr i) and 1 == 1) '#' else '.'
            }.joinToString("")
        }.joinToString("\n")

    companion object {
        fun parseLine(line: String) =
            line.toList().fold(0) { acc, c ->
                acc * 2 + when (c) {
                    '.' -> 0
                    '#' -> 1
                    else -> throw IllegalArgumentException("unexpected char $c")
                }
            }

        fun parse(input: List<String>) =
            Pattern(input.map { parseLine(it) }, input.first().length)

        fun binaryAdd(first: Int, second: Int, maxMore: Int): Int? {
            if (maxMore > 1) {
                throw IllegalArgumentException("maxMore must be <= 1")
            }
            if (first == second) {
                return 0
            }
            if (maxMore == 0 && first != second) {
                return null
            }

            val x = first xor second
            val nDiff = Integer.bitCount(x)
            if (nDiff != 1) {
                return null
            }
            if (first > second) {
                return null
            }
            return 1
        }
    }
}

fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)

    fun patterns(input: List<String>): List<Pattern> =
        input.joinToString("\n").split("\n\n").map { Pattern.parse(it.lines()) }


    fun part1(input: List<String>): Int {
        return patterns(input).sumOf {
            it.findAllMirrorRows(0).sum() * 100 + it.findAllMirrorCols(0).sum()
        }
    }

    fun part2(input: List<String>): Int {
        return patterns(input).sumOf {
            println(it.findAllMirrorRows(1))
            it.findAllMirrorRows(1).sum() * 100
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput)
    println(p1)
    check(part1(testInput) == 405)

    val p2 = part2(testInput)
    println(p2)
    check(p2 == 400)

    val input = readInput("day$day/input")
    part1(input).println()
    part2(input).println()
}
