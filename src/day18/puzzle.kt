package day18

import and
import not
import or
import println
import readInput
import set
import shiftE
import shiftN
import shiftS
import shiftW
import kotlin.math.max
import kotlin.math.min

data class LavaMapInstruction(val direction: Pair<Int, Int>, val step: Int, val color: Int) {
    companion object {
        fun parse(input: String): LavaMapInstruction {
            val re = """([UDLR]) (\d+) \(#([a-f0-9]+)\)""".toRegex()

            val (direction, step, color) = re.matchEntire(input)!!.destructured
            val dirMap = mapOf(
                "U" to (-1 to 0),
                "L" to (0 to -1),
                "D" to (1 to 0),
                "R" to (0 to 1)
            )
            return LavaMapInstruction(dirMap[direction]!!, step.toInt(), color.toInt(16))
        }

    }
}

data class LavaMapInstructionList(val instructions: List<LavaMapInstruction>) {


    fun perimeterMap(): List<List<Boolean>> {
        val boundMax = findBoundariesMax()
        val boundMin = findBoundariesMin()
        val emptyMap =
            List(boundMax.first - boundMin.first + 1) { List(boundMax.second - boundMin.second + 1) { false } }
        return instructions.flatMap { inst -> List(inst.step) { inst.direction } }
            .fold((0 to 0) to emptyMap) { (pos, map), shift ->
                val p = pos.first + shift.first to pos.second + shift.second
                p to map.set(p.first - boundMin.first, p.second - boundMin.second, true)
            }.second

    }

    fun findBoundariesMax() =
        instructions.fold((0 to 0) to (Int.MIN_VALUE to Int.MIN_VALUE)) { (pos, bound), inst ->
            val newPos = pos.first + inst.direction.first * inst.step to pos.second + inst.direction.second * inst.step
            val newBound = max(bound.first, newPos.first) to max(bound.second, newPos.second)
            newPos to newBound
        }.second.let { it.first to it.second }

    fun findBoundariesMin() =
        instructions.fold((0 to 0) to (Int.MAX_VALUE to Int.MAX_VALUE)) { (pos, bound), inst ->
            val newPos = pos.first + inst.direction.first * inst.step to pos.second + inst.direction.second * inst.step
            val newBound = min(bound.first, newPos.first) to min(bound.second, newPos.second)
            newPos to newBound
        }.second.let { it.first to it.second }

    fun isTurningRight() =
        instructions.zipWithNext().sumOf { (a, b) ->
            val x = if (isRightTurn(a.direction, b.direction)) 1 else -1
            x
        } > 0

    fun perimeter() = instructions.flatMap { inst -> List(inst.step) { inst.direction } }
        .scan((0 to 0)) { pos, shift ->
            pos.first + shift.first to pos.second + shift.second
        }.drop(1)

    fun findInnerPosition(): Pair<Int, Int> {
        val ps = perimeter()
        if (isTurningRight()) {
            return ps.windowed(3).first { (a, b, c) ->
                // R -> D
                b.second - a.second == 1 && b.first - a.first == 0 && c.first - b.first == 1 && c.second - b.second == 0
            }.let { (a, _, c) ->
                a.first + 1 to c.second - 1
            }
        } else {
            return ps.windowed(3).first { (a, b, c) ->
                // R -> U
                b.second - a.second == 1 && b.first - a.first == 0 && c.first - b.first == -1 && c.second - b.second == 0
            }.let { (a, _, c) ->
                a.first - 1 to c.second - 1
            }
        }
    }

    fun innerArea(): List<List<Boolean>> {
        val (ir, ic) = findInnerPosition()

        val boundMax = findBoundariesMax()
        val boundMin = findBoundariesMin()

        val initMap =
            List(boundMax.first - boundMin.first + 1) { List(boundMax.second - boundMin.second + 1) { false } }
                .set(ir - boundMin.first, ic - boundMin.second, true)

        val perim = perimeterMap()

        fun handler(acc: List<List<Boolean>>): List<List<Boolean>> {
            val newMap = acc.or(
                acc.shiftN(false)
                    .or(acc.shiftS(false))
                    .or(acc.shiftE(false))
                    .or(acc.shiftW(false))
                    .and(perim.not())
            )
            if (newMap == acc) {
                return newMap
            }
            return handler(newMap)
        }
        return handler(initMap).or(perim)
    }

    companion object {
        fun parse(input: List<String>): LavaMapInstructionList {
            return LavaMapInstructionList(input.map { LavaMapInstruction.parse(it) })
        }

        fun isRightTurn(a: Pair<Int, Int>, b: Pair<Int, Int>) =
            a.second * b.first - a.first * b.second > 0

    }
}


fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)

    fun part1(input: List<String>): Int {
        val map = LavaMapInstructionList.parse(input)
        return map.innerArea().sumOf { it.count { it } }
    }

    fun part2(input: List<String>): Int {

        return 42
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput)
    println(p1)
    check(part1(testInput) == 62)

//    val p2 = part2(testInput)
//    println(p2)
//    check( p2 == 281)

    val input = readInput("day$day/input")
    part1(input).println()
    part2(input).println()
}
