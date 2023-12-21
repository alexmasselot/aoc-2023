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
import toString
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

enum class Heading(val dir: Pair<Int, Int>) {
    R(0 to 1), D(1 to 0), L(0 to -1), U(-1 to 0);

    companion object {
        val list = listOf(R, D, L, U)
        val clockwise = mapOf(
            R to D, D to L, L to U, U to R
        )
        val mirror = mapOf(
            R to L, D to D, L to R, U to U
        )
    }
}

data class LavaMapInstruction(val heading: Heading, val step: Int) {
    override fun toString() = "$heading $step"

    companion object {
        fun parse(input: String, part: Int = 1): LavaMapInstruction {
            val re = """([UDLR]) (\d+) \(#([a-f0-9]+)\)""".toRegex()

            val (direction, step, color) = re.matchEntire(input)!!.destructured
            val turningMap = mapOf(
                "R" to Heading.R,
                "D" to Heading.D,
                "L" to Heading.L,
                "U" to Heading.U,
            )
            if (part == 1) {
                return LavaMapInstruction(turningMap[direction]!!, step.toInt())
            }
            val turn = Heading.list[color.last().toString().toInt()]
            return LavaMapInstruction(turn, color.dropLast(1).toInt(16))

        }

    }
}

data class LavaMap(val instructions: List<LavaMapInstruction>) {/*---------- Part 1 ------------*/

    fun perimeterMap(): List<List<Boolean>> {
        val boundMax = findBoundariesMax()
        val boundMin = findBoundariesMin()
        val emptyMap =
            List(boundMax.first - boundMin.first + 1) { List(boundMax.second - boundMin.second + 1) { false } }
        return instructions.flatMap { inst -> List(inst.step) { inst.heading.dir } }
            .fold((0 to 0) to emptyMap) { (pos, map), shift ->
                val p = pos.first + shift.first to pos.second + shift.second
                p to map.set(p.first - boundMin.first, p.second - boundMin.second, true)
            }.second

    }

    fun findBoundariesMax() = instructions.fold((0 to 0) to (Int.MIN_VALUE to Int.MIN_VALUE)) { (pos, bound), inst ->
        val newPos = pos.first + inst.heading.dir.first * inst.step to pos.second + inst.heading.dir.second * inst.step
        val newBound = max(bound.first, newPos.first) to max(bound.second, newPos.second)
        newPos to newBound
    }.second.let { it.first to it.second }

    fun findBoundariesMin() = instructions.fold((0 to 0) to (Int.MAX_VALUE to Int.MAX_VALUE)) { (pos, bound), inst ->
        val newPos = pos.first + inst.heading.dir.first * inst.step to pos.second + inst.heading.dir.second * inst.step
        val newBound = min(bound.first, newPos.first) to min(bound.second, newPos.second)
        newPos to newBound
    }.second.let { it.first to it.second }

    fun isTurningRight() = instructions.zipWithNext().sumOf { (a, b) ->
        val x = if (isRightTurn(a.heading.dir, b.heading.dir)) 1 else -1
        x
    } > 0

    fun perimeter() =
        instructions.flatMap { inst -> List(inst.step) { inst.heading.dir } }.scan((0 to 0)) { pos, shift ->
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
            List(boundMax.first - boundMin.first + 1) { List(boundMax.second - boundMin.second + 1) { false } }.set(
                ir - boundMin.first,
                ic - boundMin.second,
                true
            )

        val perim = perimeterMap()

        fun handler(acc: List<List<Boolean>>): List<List<Boolean>> {
            val newMap = acc.or(
                acc.shiftN(false).or(acc.shiftS(false)).or(acc.shiftE(false)).or(acc.shiftW(false)).and(perim.not())
            )
            if (newMap == acc) {
                return newMap
            }
            return handler(newMap)
        }
        return handler(initMap).or(perim)
    }

    /*---------------- part 2 -----------*/

    fun grittyArea(): Long {
        tailrec fun handler(acc: Long, emptyRotation: Int, lm: LavaMap): Long {
            println("---------------------- $acc")
            println(lm.instructions)
            //println(lm.perimeterMap().toString('#'))

            val reduced = lm.reduceOne()
            if (reduced == null) {
                if (emptyRotation == 3) {
                    val n = lm.instructions.size / 2
                    return handler(
                        acc, emptyRotation + 1, LavaMap(lm.instructions.drop(n).plus(lm.instructions.take(n)))
                    )
                }
                if (emptyRotation == 8) {
                    return handler(
                        acc, emptyRotation + 1, lm.mirror()
                    )
                }
                if (emptyRotation == 12) {
                    return acc
                }
                return handler(acc, emptyRotation + 1, lm.rotate())
            }
            val (newLm, newAcc) = reduced
            println((">>>>>>>>>>>>>>>>>>>>>"))
            println(newLm.instructions)
//            println(newLm.perimeterMap().toString('#'))
//            println(newLm.cleanUpSteps().instructions)
//            println(newLm.cleanUpSteps().rotate().instructions)

            return handler(acc + newAcc, 0, newLm.cleanUpSteps().rotate())
        }
        return handler(0, 0, this)
    }

    /**
     * Remove one block and return the new map + the reduced map
     */
    fun reduceOne(): Pair<LavaMap, Long>? {
        /** remove final back and forth */
        if (instructions.size == 2 && instructions[0].heading == Heading.R && instructions[1].heading == Heading.L) {
            return LavaMap(listOf()) to (max(instructions[0].step, instructions[1].step).toLong() + 1)
        }

        /** remove back and forth instruction Right/left */
        val rlBlocks = instructions.windowed(2).mapIndexed { i, ws -> i to ws }.firstOrNull() { (_, ws) ->
            /* RDL*/
            ws.map { it.heading } == listOf(Heading.R, Heading.L)
        }
        if (rlBlocks != null) {
            val (i, ws) = rlBlocks
            val alpha = ws.first().step - ws.last().step
            if (alpha > 0) {
                return LavaMap(
                    instructions.take(i).plusElement(LavaMapInstruction(Heading.R, alpha))
                        .plus(instructions.drop(i + 2))
                ) to ws.last().step.toLong()
            } else {
                return LavaMap(
                    instructions.take(i).plusElement(LavaMapInstruction(Heading.L, -alpha))
                        .plus(instructions.drop(i + 2))
                ) to ws.first().step.toLong()
            }
        }

        /** remove u-shape instructions R-D-L */
        val rdlBLock = instructions.windowed(3).mapIndexed { i, ws -> i to ws }.firstOrNull() { (_, ws) ->
            /* RDL*/
            ws.map { it.heading } == listOf(Heading.R, Heading.D, Heading.L)
        }
        if (rdlBLock != null) {
            val (i, ws) = rdlBLock
            val alpha = ws.first().step - ws.last().step
            if (alpha > 0) {
                return LavaMap(
                    instructions.take(i).plusElement(LavaMapInstruction(Heading.R, alpha)).plusElement(ws[1])
                        .plus(instructions.drop(i + 3))
                ) to (ws.last().step).toLong() * (ws[1].step + 1)
            } else {
                return LavaMap(
                    instructions.take(i).plusElement(ws[1]).plusElement(LavaMapInstruction(Heading.L, -alpha))
                        .plus(instructions.drop(i + 3))
                ) to (ws.first().step).toLong() * (ws[1].step + 1)
            }
        }
        return null
    }

    fun cleanUpSteps(): LavaMap {
        /** collapse consecutive steps in the same heading */
        val ils = instructions.windowed(2).mapIndexed { index, lavaMapInstructions -> index to lavaMapInstructions }
            .find { (_, ls) -> ls.first().heading == ls[1].heading }?.first

        val continuousInstructions = if (ils != null) {
            instructions.take(ils).plusElement(
                LavaMapInstruction(
                    instructions[ils].heading, instructions[ils].step + instructions[ils + 1].step
                )
            ).plus(
                instructions.drop(ils + 2)
            )
        } else {
            instructions
        }

        /** remove 0 steps */
        val nonZeroInstructions = continuousInstructions.filter { it.step != 0 }
        return LavaMap(
            nonZeroInstructions
        )
    }

    /**
     * rotate clockwise
     */
    fun rotate(): LavaMap {
        val newInstructions = instructions.map { LavaMapInstruction(Heading.clockwise[it.heading]!!, it.step) }
        return LavaMap(newInstructions)
    }

    fun mirror(): LavaMap {
        val newInstructions = instructions.map { LavaMapInstruction(Heading.mirror[it.heading]!!, it.step) }
        return LavaMap(newInstructions)
    }


    companion object {
        fun parse(input: List<String>, part: Int = 1): LavaMap {
            return LavaMap(input.map { LavaMapInstruction.parse(it, part) })
        }

        fun isRightTurn(a: Pair<Int, Int>, b: Pair<Int, Int>) = a.second * b.first - a.first * b.second > 0

    }
}


fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)

    fun part1(input: List<String>): Long {
        val map = LavaMap.parse(input)
        //  println(map.innerArea().sumOf { it.count { it } })
        return map.grittyArea()
    }

    fun part2(input: List<String>): Int {
        val map = LavaMap.parse(input)
        return 42
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput)
    println(p1)
    check(part1(testInput) == 62L)

//    val p2 = part2(testInput)
//    println(p2)
//    check( p2 == 281)

    val input = readInput("day$day/input")
    part1(input).println()
    part2(input).println()
}
