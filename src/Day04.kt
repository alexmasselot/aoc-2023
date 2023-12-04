import kotlin.math.max

/**
 * convert
 * Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
 * into a per of set
 * setOf(41, 48, 83, 86, 17) to setOf(83, 86,  6, 31, 17,  9, 48, 53)
 */
fun parseGame04(input: String) =
    input.split(":")[1]
        .split("|")
        .map {
            it.trim().split(" ").filter { it != "" }.map { s: String ->
                s.trim().toInt()
            }.toSet()
        }
        .let { (a, b) -> a to b }

/**
 * Given listOf(4, 2, 2, 1, 0, 0)
 * return listOf(1,2,4,8,14,1)
 *
 *  4  2  2  1  0  0 # number of matches
 *
 *  1  1  1  1  1  1
 *  1  2  2  2  2  1 # card 1 ++ 4
 *  1  2  4  4  2  1 # card 2 ++ 2
 *  1  2  4  8  6  1 # card 3 ++ 2
 *  1  2  4  8  14 1 # card 4 ++ 1
 *  1  2  4  8  14 1 # card 5 ++ 0
 *  1  2  4  8  14 1 # card 6 ++ 0
 */
fun pileCards04(ns: List<Int>): List<Int> {

    val ones = (1..ns.size).map { 1 }


    tailrec fun f(accNbCards: List<Int>, remMatches: List<Int>, remNbCards: List<Int>): List<Int> {
        if (remNbCards.isEmpty()) {
            return accNbCards
        }
        val nCard = remMatches.first()
        val newAcc = accNbCards.plus(remNbCards.first())
        val newMatches = remMatches.drop(1)
        val newRemNbCards =
            remNbCards.drop(1).take(nCard).map { a -> a + remNbCards.first() }
            .plus(remNbCards.drop(1 + nCard))
        return f(
            newAcc,
            newMatches,
            newRemNbCards
        )
    }
    return f(emptyList(), ns, ones)
}

fun main() {
    val day = "04"
    // Locate digit in a string and return the first and the last ones concatenated

    fun part1(input: List<String>): Int {
        return input
            .map { parseGame04(it) }.sumOf { (a, b) ->
                max(0, 1 shl (a.intersect(b).size - 1))
            }
    }

    fun part2(input: List<String>): Int {
        val cardsIntersection = input
            .map { parseGame04(it) }
            .map { (a, b) ->
                a.intersect(b).size
            }
        return pileCards04(cardsIntersection).sum()

    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/part1_test")
    val p1 = part1(testInput)
    println(p1)
    check(part1(testInput) == 13)

//    val testInput2 = readInput("day$day/part2_test")
    val p2 = part2(testInput)
    println(p2)
    check(p2 == 30)

    val input = readInput("day$day/part1")
    part1(input).println()
    part2(input).println()
}
