package day16

import CellularAutomata
import countIf
import mapMatrix
import println
import readInput
import toStringMat
import zipApply
import java.lang.IllegalArgumentException

data class BeamCA(val ca: CellularAutomata) {

    fun startsWith(dir: String, p: Int) = when {
        dir == "left" ->
            BeamCA(
                CellularAutomata(
                    ca.grid.take(p)
                        .plusElement(listOf(ca.grid.get(p).first() or CellularAutomata.maskE).plus(ca.grid.get(p).drop(1)))
                        .plus(ca.grid.drop(p + 1)),
                    ca.rules
                )
            )

        else -> throw IllegalArgumentException(dir)
    }

    fun next() = BeamCA(ca.next())

    fun isEnergized() = ca.grid.mapMatrix { (it and CellularAutomata.shiftedBits) != 0 }

    override fun toString() = isEnergized().mapMatrix { if (it) '#' else '.' }.toStringMat()

    companion object {
        /*
        frozen cells (no beams) are shl by 4 bit, but contains
        0 -> empty
        1 -> -
        2 -> |
        3 -> /
        4 <- \
         */
        val gridElIndex = mapOf(
            '.' to 0,
            '-' to 1,
            '|' to 2,
            '/' to 3,
            '\\' to 4
        )

        // How one beam bit react to possible nothin, splitters or mirros
        val beamFlow = listOf(
            listOf(1, 2, 4, 8),
            listOf(10, 2, 10, 8),
            listOf(1, 5, 4, 5),
            listOf(8, 4, 2, 1),
            listOf(2, 1, 8, 4),
        )
        val rules =
            (0..4).flatMap { iGrid ->
                val frozenBits = iGrid shl 4
                listOf(0, 1).flatMap { bE ->
                    val mE = bE * CellularAutomata.maskE
                    listOf(0, 1).flatMap { bS ->
                        val mS = bS * CellularAutomata.maskS
                        listOf(0, 1).flatMap { bW ->
                            val mW = bW * CellularAutomata.maskW
                            listOf(0, 1).map { bN ->
                                val mN = bN * CellularAutomata.maskN
                                // val i = frozenBits or mE or mS or mW or mN just an incremental integer
                                frozenBits or
                                        (bN * beamFlow[iGrid][0]) or
                                        (bW * beamFlow[iGrid][1]) or
                                        (bS * beamFlow[iGrid][2]) or
                                        (bE * beamFlow[iGrid][3])
                            }
                        }
                    }

                }
            }

        fun parse(input: List<String>): BeamCA {
            val grid = input.map { l ->
                l.toList().map { c ->
                    gridElIndex[c]!! shl 4
                }
            }
            return BeamCA(CellularAutomata(grid, rules))
        }
    }
}

fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)

    fun part1(input: List<String>): Int {
        val beamCA = BeamCA.parse(input).startsWith("left", 0)
        println(beamCA)

        tailrec fun handler(accBeam: List<List<Int>>, bca: BeamCA): List<List<Boolean>> {
            val next = bca.next()
            val nextBeam =
                accBeam.zipApply(next.ca.grid) { a, b -> (a and CellularAutomata.shiftedBits) or (b and CellularAutomata.shiftedBits) }
            if (accBeam == nextBeam) {
                return accBeam.mapMatrix { it != 0 }
            }
            return handler(nextBeam, next)
        }
        return handler(beamCA.ca.grid.mapMatrix { it and CellularAutomata.shiftedBits }, beamCA).countIf { it }

    }

    fun part2(input: List<String>): Int {

        return 42
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput)
    check(part1(testInput) == 46)

//    val p2 = part2(testInput)
//    println(p2)
//    check( p2 == 281)

    val input = readInput("day$day/input")
    part1(input).println()
    part2(input).println()
}
