package day11

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class UniverseTest {
    val input = """
        ...#......
        .......#..
        #.........
        ..........
        ......#...
        .#........
        .........#
        ..........
        .......#..
        #...#.....
    """.trimIndent().split("\n")

    @Test
    fun testParseInput() {
        val given = """
            .#.
            ..#
            ###
        """.trimIndent().split("\n")
        val got = Universe.parseInput(given)
        val expected = Universe(
            listOf(
                listOf(false, true, false),
                listOf(false, false, true),
                listOf(true, true, true)
            ),
            listOf(0, 1, 2),
            listOf(0, 1, 2),
        )
        assertEquals(expected, got)
    }

    @Test
    fun testShiftEmpty() {
        val given = listOf(false, false, true, true, false, false)
        val got = Universe.shiftEmpty(given, 11)

        assertEquals(listOf(0, 1, 2, 13, 24, 25), got)
    }

    @Test
    fun testExpand() {
        val u = Universe.parseInput(input)
        val got = u.expand(2)

        assertEquals(listOf(0, 1, 2, 4, 5, 6, 8, 9, 10, 12), got.colPos)
        assertEquals(listOf(0, 1, 2, 3, 5, 6, 7, 8, 10, 11), got.rowPos)
    }

    @TestFactory
    fun testDistance() = listOf(
        Pair(6L to 1L, 6L to 2L) to 1L,
        Pair(6L to 1L, 6L to 3L) to 2L,
        Pair(6L to 1L, 7L to 1L) to 1L,
        Pair(6L to 1L, 7L to 2L) to 2L,
        Pair(6L to 1L, 7L to 3L) to 3L,
        Pair(6L to 1L, 11L to 5L) to 9L,
    )
        .map { (input: Pair<Pair<Long, Long>, Pair<Long, Long>>, expected: Long) ->
            DynamicTest.dynamicTest(" $input => $expected") {
                val p1 = input.first
                val p2 = input.second

                val got = Universe.distance(p1, p2)
                assertEquals(expected, got)
            }
        }


}