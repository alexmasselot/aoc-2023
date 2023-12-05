package day05

import println
import readInput


data class PosMapping(val to: Long, val from: Long, val len: Long) {
    fun to(x: Long): Long {
        if (x < from || x >= from + len) {
            return x
        }
        return to + (x - from)
    }

    fun isMoved(x: Long): Boolean =
        x >= from && x < from + len
}

data class PosMappingList(val mappings: List<PosMapping>) {
    fun to(x: Long): Long =
        mappings.firstOrNull { it.isMoved(x) }?.let { it.to(x) } ?: x

    companion object {
        fun fromString(s: String): PosMappingList {
            val x = s.split("\n").map { it.split(" ").map { it.toLong() } }
            return PosMappingList(x.map { PosMapping(it[0], it[1], it[2]) })
        }


    }
}

fun main() {
    // this var contains the current package name
    val day = ::main.javaClass.`package`.name.takeLast(2)
    // Locate digit in a string and return the first and the last ones concatenated

    fun readSeeds(input: List<String>): List<Long> =
        input.first().split(":")[1].trim().split(" ").map { it.toLong() }

    fun readAllPosMappings(input: List<String>): List<PosMappingList> =
        input.drop(2).joinToString("\n").split("\n\n")
            .map { it.split("\n").drop(1).joinToString("\n") }
            .map { PosMappingList.fromString(it) }

    fun part1(input: List<String>): Long {
        val seeds = readSeeds(input)
        val pms = readAllPosMappings(input)
        return seeds.map { seed ->
            pms.fold(seed) { acc, pm -> pm.to(acc) }
        }.min()

    }

    fun part2(input: List<String>): Long {

        val seedRanges = readSeeds(input).chunked(2).map { (a, b) ->
            a..(a + b - 1)
        }

        val pms = readAllPosMappings(input)
        return seedRanges.map {
            it.minOfOrNull { seed ->
                pms.fold(seed) { acc, pm -> pm.to(acc) }
            }!!
        }.min()

    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput)
    println(p1)
    check(part1(testInput) == 35L)

    val p2 = part2(testInput)
    println(p2)
    check(p2 == 46L)

    val input = readInput("day$day/input")
    part1(input).println()
    part2(input).println()
}
