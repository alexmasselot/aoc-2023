package day15

import println
import readInput
import java.lang.IllegalArgumentException

data class Lens(val label: String, val focalLength: Int) {
    override fun toString() = "$label $focalLength"
}

data class Lenses(val ls: List<List<Lens>>) {
    fun process(str: String): Lenses =
        when {
            str.matches(reEquals) -> {
                val (label, focalLength) = reEquals.find(str)!!.destructured
                val lens = Lens(label, focalLength.toInt())
                val iBox = Hash.hash(label)
                if (ls[iBox].isEmpty()) {
                    Lenses(ls.take(iBox).plusElement(listOf(lens)).plus(ls.drop(iBox + 1)))
                } else {
                    val i = ls[iBox].indexOfFirst { it.label == lens.label }
                    if (i == -1) {
                        Lenses(ls.take(iBox).plusElement(ls[iBox].plus(lens)).plus(ls.drop(iBox + 1)))
                    } else {
                        Lenses(
                            ls.take(iBox).plusElement(ls[iBox].take(i).plusElement(lens).plus(ls[iBox].drop(i + 1)))
                                .plus(ls.drop(iBox + 1))
                        )
                    }
                }
            }

            str.matches(reMinus) -> {
                val (label) = reMinus.find(str)!!.destructured
                val iBox = Hash.hash(label)
                Lenses(ls.take(iBox).plusElement(ls[iBox].filter { it.label != label }).plus(ls.drop(iBox + 1)))
            }

            else -> throw IllegalArgumentException("invalid input '$str'")
        }

    fun score(): Int {
        return ls.mapIndexed { index, lenses -> index to lenses }
            .sumOf { (iBox, box) ->
                (iBox + 1) * box.mapIndexed { index, lens -> index to lens }
                    .sumOf { (i, lens) -> (i + 1) * lens.focalLength }
            }

    }

    override fun toString() =
        ls.mapIndexed { index, lenses -> index to lenses }
            .filter { it.second.isNotEmpty() }
            .joinToString("\n") { (i, lenses) -> "Box $i ${lenses.joinToString(" ") { "[$it]" }}" }


    companion object {
        val reEquals = """^(\w+)=(\d+)$""".toRegex()
        val reMinus = """^(\w+)-$""".toRegex()

        fun empty() = Lenses(List(256) { emptyList<Lens>() })
        fun fill(ps: List<Pair<Int, List<Lens>>>) =
            ps.fold(Lenses.empty()) { acc, (i, lens) ->
                Lenses(
                    acc.ls.take(i).plusElement(lens).plus(acc.ls.drop(i + 1))
                )
            }

    }
}

object Hash {

    fun hash(input: String) =
        input.fold(0) { acc, c -> hashChar(c, acc) }

    fun hashChar(c: Char, v: Int) =
        ((v + c.code) * 17) % 256


}

fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)

    fun part1(input: List<String>): Int {
        return input.first().split(",").sumOf { Hash.hash(it) }
    }

    fun part2(input: List<String>): Int {
        return input.first().split(",")
            .fold(Lenses.empty()) { acc, s -> acc.process(s) }
            .score()

    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput)
    println(p1)
    check(part1(testInput) == 1320)

    val p2 = part2(testInput)
    println(p2)
    check( p2 == 145)

    val input = readInput("day$day/input")
    part1(input).println()
    part2(input).println()
}
