package day22

import println
import readInput

fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)

    fun part1(input: List<String>): Int {
        val brickMap = BrickMap.parse(input)
        return return brickMap.size - brickMap.indexesMonoSupports().size
    }

    fun part2(input: List<String>): Int {
        val brickMap = BrickMap.parse(input)
        val dropper = BricksDropper(brickMap.isCatchingMatrix())
        val grounded = dropper.dropThemAll(brickMap)
        val iDisintegrate = grounded.indexesMonoSupports()

        fun disintegrateAndCount(i: Int): Int {
            val b = grounded.bricks[i]
            val newBricks = grounded.bricks.take(i)
                .plus(Brick(b.p1, Point(b.p2.x, b.p2.y, b.p1.z - 1)))
                .plus(grounded.bricks.drop(i + 1))
            val shaken = dropper.dropThemAll(BrickMap(newBricks))
            return shaken.bricks.zip(grounded.bricks).count { it.first != it.second } - 1
        }
        return iDisintegrate.sumOf {
            val n = disintegrateAndCount(it)
            println("$it -> $n")
            n
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput)
    println(p1)
    check(part1(testInput) == 5)

    val p2 = part2(testInput)
    println(p2)
    check( p2 == 7)

    val input = readInput("day$day/input")
    part1(input).println()
    part2(input).println()
}
