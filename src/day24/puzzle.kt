package day24

import println
import readInput



fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)

    fun part1(input: List<String>, bMin: Long, bMax: Long): Int {
        val stones = input.map { HailStone.parse(it) }
        return stones.mapIndexed { index, hailStone ->  index to hailStone }
            .sumOf{(index, hailStone) ->
                stones.drop(index+1).mapNotNull { other ->
                    hailStone.trajectoryIntersect2DInFuture(other)
                }.count { (x, y) ->
                    x>=bMin && x<=bMax && y>=bMin && y<=bMax
                }
            }
    }

    fun part2(input: List<String>): Int {

        return 42
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput, 7, 27 )
    println(p1)
    check(p1 == 2)

//    val p2 = part2(testInput)
//    println(p2)
//    check( p2 == 281)

    val input = readInput("day$day/input")
    part1(input, 200000000000000L, 400000000000000L ).println()
    part2(input).println()
}
