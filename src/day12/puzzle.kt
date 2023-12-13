package day12

import println
import readInput

object BlockFinder {
    val regexes = (0..20).map { len ->
        listOf("""^[\.\?]*?([\?#]{$len})(?:[\.\?]|$)""".toRegex())
            .plus((1..100).map { """^[\.\?]{$it,100}?([\?#]{$len})(?:[\.\?]|$)""".toRegex() })
    }

    fun findBlock(input: String, len: Int, offset: Int = 0): Int? =
        regexes[len][offset].find(input)?.groups?.get(1)?.range?.first


    /**
     * find the starting position s of blocks of size len in input string, without any # before
     */
    fun findAllBlocks(input: String, len: Int): Sequence<Int> {
        return generateSequence(findBlock(input, len)) { n ->
            val r = findBlock(input, len, n + 1)
            r
        }
    }

    fun countBlocks(input: String, lens: List<Int>): Int {
        val remainsNone = """^[\.\?]*$""".toRegex()
        tailrec fun handler(acc: Int, rInput: String, rLens: List<Int>): Int {
            if (rLens.isEmpty())
                if (rInput.matches(remainsNone)) {
                    return 1
                } else {
                    return 0
                }


            val posNext = findAllBlocks(rInput, rLens.first()).toList()
            return posNext.map { p ->
                handler(
                    acc,
                    rInput.drop(p + rLens.first() + 1),
                    rLens.drop(1)
                )
            }.sum()

        }

        return handler(0, input, lens)
    }

    private val reParse = """(\S+)\s+(\S+)""".toRegex()
    fun parseLine(input: String, times: Int): Pair<String, List<Int>> {
        val (str, lens) = reParse.matchEntire(input)!!.destructured
        return (1..times).map { str }.joinToString("?") to
                (1..times).map { lens }.joinToString(",").split(",").map { it.toInt() }
    }
}

fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)

    fun part1(input: List<String>): Int {
        return input.map { l ->
            val (str, lens) = BlockFinder.parseLine(l, 1)
            BlockFinder.countBlocks(str, lens)
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return 42
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput)
    println(p1)
    check(part1(testInput) == 21)

//    val p2 = part2(testInput)
//    println(p2)
//    check( p2 == 281)

    val input = readInput("day$day/input")
    part1(input).println()
    part2(input).println()
}
