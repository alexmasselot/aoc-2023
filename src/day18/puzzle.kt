package day18

import day18.LavaMapInstruction.incPosition
import day18.LavaMapInstruction.moveMap
import println
import set
import readInput
import toString
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


object LavaMapInstruction {
    val moveMap = mapOf(
        "R" to (0 to 1),
        "L" to (0 to -1),
        "U" to (-1 to 0),
        "D" to (1 to 0),
    )
    val incPositionFunctions = moveMap.map { (c, d) ->
        c to { p: Pair<Int, Int>, f: Int -> p.first + f * d.first to p.second + f * d.second }
    }.toMap()


    fun incPosition(step: String, pos: Pair<Int, Int>): Pair<Int, Int> {
        val re = """([RLUD])(\d+)""".toRegex()
        val (c, d) = re.matchEntire(step)!!.destructured
        return incPositionFunctions[c]!!(pos, d.toInt())
    }

    fun parse(input: String, part: Int = 1): String {
        val re = """([UDLR]) (\d+) \(#([a-f0-9]+)\)""".toRegex()

        val (direction, step, color) = re.matchEntire(input)!!.destructured
        if (part == 1) {
            return "$direction$step"
        }
        val headingList = listOf("R", "D", "L", "U")
        return "${headingList[color.last().toString().toInt()]}${color.dropLast(1).toInt(16)}"
    }

}


data class LavaMap(val line: String) {/*---------- Part 1 ------------*/

    fun area(): Long {
//        println("----------")
//        println(perimeterMap().toString('#'))
        tailrec fun handler(acc: Long, l: String): Long {
//            println("----------")
//            println("$acc $l")
//            println(LavaMap(l).perimeterMap().toString('#'))
            val (l1, a1) = replaceTriplet(l) ?: return acc
//            println("---------- l1")
//            println("$a1 $l1")
//            println(LavaMap(l1).perimeterMap().toString('#'))
            val l2 = mergeContinuousInstructions(l1)
//            println("---------- l2")
//            println("$l2")
//            println(LavaMap(l2).perimeterMap().toString('#'))
            val (l3, a3) = replaceBackAndForth(l2)
//            println("---------- l3")
//            println("$a3 $l3")
//            println(LavaMap(l3).perimeterMap().toString('#'))
            return handler(acc + a1 + a3, l3)
        }
        return handler(0L, line)+1
    }

    fun perimeterMap(): List<List<Boolean>> {
        val boundMax = findBoundariesMax()
        val boundMin = findBoundariesMin()
        val emptyMap =
            List(boundMax.first - boundMin.first + 1) { List(boundMax.second - boundMin.second + 1) { false } }

        return """([RLUD])(\d+)""".toRegex().findAll(line).flatMap {
            val (c, d) = it.destructured
            List(d.toInt()) { moveMap[c]!! }
        }
            .fold((0 to 0) to emptyMap) { (pos, map), shift ->
                val p = pos.first + shift.first to pos.second + shift.second
                p to map.set(p.first - boundMin.first, p.second - boundMin.second, true)
            }.second
    }

    fun findBoundariesMax() = """[RLUD]\d+""".toRegex().findAll(line).map { it.value }
        .fold((0 to 0) to (Int.MIN_VALUE to Int.MIN_VALUE)) { (pos, bound), inst ->
            val newPos = incPosition(inst, pos)
            val newBound = max(bound.first, newPos.first) to max(bound.second, newPos.second)
            newPos to newBound
        }.second.let { it.first to it.second }

    fun findBoundariesMin() = """[RLUD]\d+""".toRegex().findAll(line).map { it.value }
        .fold((0 to 0) to (Int.MAX_VALUE to Int.MAX_VALUE)) { (pos, bound), inst ->
            val newPos = incPosition(inst, pos)
            val newBound = min(bound.first, newPos.first) to min(bound.second, newPos.second)
            newPos to newBound
        }.second.let { it.first to it.second }

    fun isTurningRight(): Boolean {
        val rights = listOf("R" to "D", "D" to "L", "L" to "U", "U" to "R").map { (a, b) -> """$a\d+$b\d+""".toRegex() }
        val lefts = listOf("R" to "U", "U" to "L", "L" to "D", "D" to "R").map { (a, b) -> """$a\d+$b\d+""".toRegex() }
        return rights.sumOf { it.findAll(line).count() } - lefts.sumOf { it.findAll(line).count() } > 0
    }


    companion object {
        fun parse(input: List<String>, part: Int = 1): LavaMap {
            return LavaMap(input.map { LavaMapInstruction.parse(it, part) }.joinToString(""))
        }

        /** replace a right turning by a pair of instruction and return the new string and the area removed*/
        fun replaceTriplet(str: String): Pair<String, Long>? =
            matchTriplet(str)?.let { matchResults ->
                val (red, area) = reduceTriplet(matchResults)
                str.take(matchResults.range.first) + red + str.drop(matchResults.range.last + 1) to area
            } ?: null



        private val reTriplets = listOf("RDL", "DLU", "LUR", "URD").map {
            val (h1, h2, h3) = it.toList()
            """($h1)(\d+)($h2)(\d+)($h3)(\d+)""".toRegex()
        }

        fun matchTriplet(str: String) = reTriplets.flatMap {
            it.findAll(str)
        }.minByOrNull { it.groupValues[4].toLong() }

        fun reduceTriplet(matchResults: MatchResult): Pair<String, Long> {
            val (h1, s1, h2, s2, h3, s3) = matchResults.destructured
            val i1 = s1.toLong()
            val i2 = s2.toLong()
            val i3 = s3.toLong()
            val n = min(i1, i3) * (i2+1)
            if (i1 > i3) {
                return "$h1${i1 - i3}$h2$i2" to n
            } else if (i1 < i3) {
                return "$h2$i2$h3${i3 - i1}" to n
            } else {
                return "$h2$i2" to n
            }
            throw NotImplementedError()
        }

        /** mege if we have two or more repapeat orders in the same directions */
        tailrec fun mergeContinuousInstructions(str: String): String {
            val re = """([RLUD])(\d+)\1(\d+)""".toRegex()

            return re.find(str)?.let { mr ->
                val (direction, s1, s2) = mr.destructured
                mergeContinuousInstructions(
                    str.take(mr.range.first) + "$direction${s1.toInt() + s2.toInt()}" + str.drop(
                        mr.range.last + 1
                    )
                )
            } ?: str
        }

        /** merge if we have back and forth instructions */
        val reBackAndForth = listOf("RL", "LR", "UD", "DU").map {
            val (h1, h2) = it.toList()
            """($h1)(\d+)($h2)(\d+)""".toRegex()
        }

        fun replaceBackAndForth(str: String): Pair<String, Long> {
            tailrec fun handler(acc: Long, s: String): Pair<String, Long> {
                val re = reBackAndForth.firstNotNullOfOrNull {
                    it.find(s)
                } ?: return s to acc
                val (h1, s1, h2, s2) = re.destructured
                val i1 = s1.toLong()
                val i2 = s2.toLong()
                val prefix = s.take(re.range.first)
                val suffix = s.drop(re.range.last + 1)
                if (i1 > i2) {
                    return handler(acc + i2, prefix + "$h1${i1 - i2}" + suffix)
                } else {
                    return handler(acc + i1, prefix + "$h2${i2 - i1}" + suffix)
                }
            }

            return handler(0L, str)
        }
    }
}


fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)

    fun part1(input: List<String>): Long {
        val map = LavaMap.parse(input)
        return map.area()
    }

    fun part2(input: List<String>): Long {
        val map = LavaMap.parse(input, part = 2)
        return map.area()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput)
    println(p1)
    check(part1(testInput) == 62L)

    val p2 = part2(testInput)
    println(p2)
    check( p2 == 952408144115L)

    val input = readInput("day$day/input")
    part1(input).println()
    part2(input).println()
}
