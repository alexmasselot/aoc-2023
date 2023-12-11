package day11

import day10.PipeUnit
import org.junit.jupiter.api.Assertions.*
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
            )
        )
        assertEquals(expected, got)
    }

    @Test
    fun testExpand(){
        val u = Universe.parseInput(input)
        val got = u.expand()

        val expected = """
            ....#........
            .........#...
            #............
            .............
            .............
            ........#....
            .#...........
            ............#
            .............
            .............
            .........#...
            #....#.......
        """.trimIndent()

        assertEquals(expected, got.toString())
    }

    @TestFactory
    fun testDistance() = listOf(
        Pair(6 to 1, 6 to 2) to 1,
        Pair(6 to 1, 6 to 3) to 2,
        Pair(6 to 1, 7 to 1) to 1,
        Pair(6 to 1, 7 to 2) to 2,
        Pair(6 to 1, 7 to 3) to 3,
        Pair(6 to 1, 11 to 5) to 9,
    )
        .map { (input: Pair<Pair<Int, Int>, Pair<Int, Int>>, expected: Int) ->
            DynamicTest.dynamicTest(" $input => $expected") {
                val p1 = input.first
                val p2 = input.second

                val got = Universe.distance(p1, p2)
                assertEquals(expected, got)
            }
        }


}