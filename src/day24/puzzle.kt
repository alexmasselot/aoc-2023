package day24

import println
import readInput
import toPrimeFactors
import kotlin.math.abs
import kotlin.math.ceil


fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)

    fun part1(input: List<String>, bMin: Long, bMax: Long): Int {
        val stones = input.map { HailStone.parse(it) }

        return stones.mapIndexed { index, hailStone -> index to hailStone }
            .sumOf { (index, hailStone) ->
                stones.drop(index + 1).mapNotNull { other ->
                    hailStone.trajectoryIntersect2DInFuture(other)
                }.count { (x, y) ->
                    x >= bMin && x <= bMax && y >= bMin && y <= bMax
                }
            }
    }

    fun checkPart2(input: List<String>) {
        val stones = input.map { HailStone.parse(it) }
        val striker =
            HailStone(Position(1191146615936494, 342596108503183, -216265564473909), Velocity(139, -93, 245))
        // HailStone(Position(24, 13, 10), Velocity(-3, 1, 2))
        stones.forEach { st ->
            println(st.trajectoryIntersect3D(striker))
        }
    }

    fun part2(input: List<String>): Int {
        val stones = input.map { HailStone.parse(it) }//.plus(HailStone(Position(24, 13, 10), Velocity(-3, 1, 2)))


        stones.mapIndexed { index, hailStone -> index to hailStone }
            .forEach { (index, hailStone) ->
                stones.drop(index + 1).mapNotNull { other ->
                    hailStone.trajectoryIntersect3D(other)?.let { p ->
                        println("intersect:\n$hailStone\n$other")
                        println(p)
                    }
                    if (hailStone.isColinear(other)) {
                        println("colinear: \n$hailStone\n$other")
                    }
                }
            }

        val commonX = stones.mapIndexed { index, hailStone -> index to hailStone }
            .groupBy { it.second.vel.x }
            .filter { it.value.size > 1 }
            .map { it.value.map { it.first } }
            .toList()
        val commonY = stones.mapIndexed { index, hailStone -> index to hailStone }
            .groupBy { it.second.vel.y }
            .filter { it.value.size > 1 }
            .map { it.value.map { it.first } }
            .toList()
        val commonZ = stones.mapIndexed { index, hailStone -> index to hailStone }
            .groupBy { it.second.vel.z }
            .filter { it.value.size > 1 }
            .map { it.value.map { it.first } }
            .toList()

        println(commonX)
        println(commonY)
        println(commonZ)
        println(commonX.intersect(commonY).intersect(commonZ))

        fun printDeltaPrimes(a: Long, b: Long, v: Int) {
            println("${b - a}: ${toPrimeFactors(abs(b - a))} ${(b - a) / v} ${(1.0*(b - a) / v)-ceil((1.0*(b - a) / v))}")
        }

        // thus was found by looking at common factors of the deltas of the positions. But not always the GCD
        val vel = Velocity(139, -93, 245)

        stones.groupBy { it.vel.x }
            .filter { it.value.size > 2 }
            .toList()
            .sortedBy { it.first }
            .take(6)
            .forEach { (k, v) ->
                println("velocity X = $k")
                v.forEach { println("\t${it}") }
                printDeltaPrimes(v[0].pos.x, v[1].pos.x, vel.x-k)
                printDeltaPrimes(v[0].pos.x, v[2].pos.x, vel.x-k)
                printDeltaPrimes(v[1].pos.x, v[2].pos.x, vel.x-k)
            }
        stones.groupBy { it.vel.y }
            .filter { it.value.size > 2 }
            .toList()
            .sortedBy { it.first }
            .take(6)
            .forEach { (k, v) ->
                println("velocity Y = $k")
                v.forEach { println("\t${it}") }
                printDeltaPrimes(v[0].pos.y, v[1].pos.y, vel.y)
                printDeltaPrimes(v[0].pos.y, v[2].pos.y, vel.y)
                printDeltaPrimes(v[1].pos.y, v[2].pos.y, vel.y)
            }
        stones.groupBy { it.vel.z }
            .filter { it.value.size > 2 }
            .toList()
            .sortedBy { it.first }
            .take(6)
            .forEach { (k, v) ->
                println("velocity Z = $k")
                v.forEach { println("\t${it}") }
                printDeltaPrimes(v[0].pos.z, v[1].pos.z, vel.z)
                printDeltaPrimes(v[0].pos.z, v[2].pos.z, vel.z)
                printDeltaPrimes(v[1].pos.z, v[2].pos.z, vel.z)
            }
        val pos = Position(191146615936494, 342596108503183, 0)

        return 42
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput, 7, 27)
    println(p1)
    check(p1 == 2)

    val p2 = part2(testInput)
    println(p2)
//check( p2 == 47)

    val input = readInput("day$day/input")
    part1(input, 200000000000000L, 400000000000000L).println()
    checkPart2(input)
    part2(input).println()
}
