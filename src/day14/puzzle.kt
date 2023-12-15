package day14

import and
import mapMatrix
import mask
import not
import or
import println
import readInput
import rotateClockwise
import shiftN
import shiftS
import toStringMat
import kotlin.system.measureTimeMillis

data class World(val isBlock: List<List<Boolean>>, val isRoll: List<List<Boolean>>, val isEmpty: List<List<Boolean>>) {
    fun rollNorth(): World {
        fun handler(w: World): World {
            val tmpNewRoll = w.isEmpty.and(w.isRoll.shiftN(false))
            val tmpNewEmpty = w.isRoll.and(w.isEmpty.shiftS(false))
            val newRoll = tmpNewRoll.or(w.isRoll.and(tmpNewEmpty.not()))
            val newEmpty = tmpNewEmpty.or(w.isEmpty.and(tmpNewRoll.not()))
            if (newRoll == w.isRoll)
                return w
            return handler(World(w.isBlock, newRoll, newEmpty))
        }
        return handler(this)
    }

    fun load() =
        isRoll.mapIndexed { index, booleans -> (isRoll.size - index) * booleans.count { it } }.sum()

    fun rotateClockWise() = World(isBlock.rotateClockwise(), isRoll.rotateClockwise(), isEmpty.rotateClockwise())

    fun tilt() =
        rollNorth().rotateClockWise().rollNorth().rotateClockWise().rollNorth().rotateClockWise().rollNorth()
            .rotateClockWise()

    fun findLoop(): Pair<Int, Int> {
        val memoize = mutableMapOf<String, Int>()
        tailrec fun handler(i: Int, w: World): Pair<Int, Int> {
            val s = w.toString()
            if (s in memoize) {
                return i to memoize[s]!!
            }
            memoize[s] = i
            return handler(i + 1, w.tilt())
        }
        return handler(0, this)
    }

    fun tiltN(n: Long): World {
        val (to, from) = findLoop()
        println("loop $to -> $from")
        val nEff = ((n - from.toLong()) % (to - from) + from).toInt()
        println("nEff = $nEff")
        return (0..<nEff).fold(this) { acc, _ -> acc.tilt() }
    }

    override fun toString(): String {
        return isBlock.mapMatrix { if (it) '#' else '.' }.mask(isRoll.not(), 'O').toStringMat()
    }


    companion object {
        fun parse(input: List<String>) = World(
            input.map { l -> l.toList().map { it == '#' } },
            input.map { l -> l.toList().map { it == 'O' } },
            input.map { l -> l.toList().map { it == '.' } }
        )

    }
}

fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)

    fun part1(input: List<String>): Int {
        val w = World.parse(input)
        return w.rollNorth().load()
    }

    fun part2(input: List<String>): Int {
        val w = World.parse(input)
        return w.tiltN(1000000000L).load()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput)
    println(p1)
    check(part1(testInput) == 136)

    val p2 = part2(testInput)
    println(p2)
    check( p2 == 64)

    val input = readInput("day$day/input")
    part1(input).println()
    part2(input).println()
}
