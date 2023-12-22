package day18

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import toString

class LavaMapTest {
    val input = """
        R 6 (#70c710)
        D 5 (#0dc571)
        L 2 (#5713f0)
        D 2 (#d2c081)
        R 2 (#59c680)
        D 2 (#411b91)
        L 5 (#8ceee2)
        U 2 (#caa173)
        L 1 (#1b58a2)
        U 2 (#caa171)
        R 2 (#7807d2)
        U 3 (#a77fa3)
        L 2 (#015232)
        U 2 (#7a21e3)
    """.trimIndent()

    @Test
    fun testParse1() {
        val given = "R 6 (#70c710)"

        val got = LavaMapInstruction.parse(given, part = 1)

        assertEquals("R6", got)
    }

    @Test
    fun testParse2() {
        val given = "R 6 (#70c710)"

        val got = LavaMapInstruction.parse(given, part = 2)

        assertEquals("R461937", got)

    }

    @TestFactory
    fun testIncPosition() = listOf(
        "R5" to (10 to 105),
        "L5" to (10 to 95),
        "U5" to (5 to 100),
        "D5" to (15 to 100),
    ).map { (str: String, expected: Pair<Int, Int>) ->
        DynamicTest.dynamicTest(" $str -> $expected") {
            val got = LavaMapInstruction.incPosition(str, 10 to 100)
            assertEquals(expected, got)
        }
    }


    @Test
    fun testFindBoundariesMax() {
        val map = LavaMap.parse(input.lines())

        val got = map.findBoundariesMax()

        assertEquals(9 to 6, got)
    }

    @Test
    fun testFindBoundariesMin() {
        val map = LavaMap.parse(input.lines())

        val got = map.findBoundariesMin()

        assertEquals(0 to 0, got)
    }

    @Test
    fun testPerimeterMap() {
        val given = LavaMap.parse(input.lines())

        val got = given.perimeterMap()

        val expected = """
            #######
            #.....#
            ###...#
            ..#...#
            ..#...#
            ###.###
            #...#..
            ##..###
            .#....#
            .######
        """.trimIndent()

        assertEquals(expected, got.toString('#'))
    }

    @Test
    fun testIsTurningRight() {
        val given = LavaMap.parse(input.lines())

        val got = given.isTurningRight()

        assertTrue(got)
    }

    @TestFactory
    fun testMatchReduceTriplet() = listOf(
        "R5D3L2" to ("R3D3" to 8L),
        "R2D3L5" to ("D3L3" to 8L),
        "U2R3D5" to ("R3D3" to 8L),
        "U5R3D2" to ("U3R3" to 8L),
        "L5U4R6" to ("U4R1" to 25L),
        "D5L4U6" to ("L4U1" to 25L),
        "D5L4U6" to ("L4U1" to 25L),
    ).map { (given: String, expected: Pair<String, Long>) ->
        DynamicTest.dynamicTest(" $given -> $expected") {
            val mr = LavaMap.matchTriplet(given)
            assertNotNull(mr)
            val got = LavaMap.reduceTriplet(mr!!)
            assertEquals(expected, got)
        }
    }

    @TestFactory
    fun testReplaceTriplet() = listOf(
        "xxxxR5D3L2yyy" to ("xxxxR3D3yyy" to 8L),
        "xxxxR2D3L5" to ("xxxxD3L3" to 8L),
        "U2R3D5yyy" to ("R3D3yyy" to 8L),
        "U5R3D2" to ("U3R3" to 8L),
        "U5L3D2" to null,
    ).map { (given: String, expected: Pair<String, Long>?) ->
        DynamicTest.dynamicTest(" $given -> $expected") {
            val got = LavaMap.replaceTriplet(given)
            assertEquals(expected, got)
        }
    }
    @TestFactory
    fun testReplaceBackAndForth() = listOf(
        "xxxxR5L2yyy" to ("xxxxR3yyy" to 2L),
        "xxxxR2L5" to ("xxxxL3" to 2L),
        "U2D5yyy" to ("D3yyy" to 2L),
        "U3D2U7D3" to ("U1U4" to 5L),
        "U3D10" to ("D7" to 3L),
        "U3D10U4" to ("D3" to 7L),
        "U5L3D2" to ("U5L3D2" to 0L),
    ).map { (given: String, expected: Pair<String, Long>) ->
        DynamicTest.dynamicTest(" $given -> $expected") {
            val got = LavaMap.replaceBackAndForth(given)
            assertEquals(expected, got)
        }
    }

    @TestFactory
    fun testMergeContinousInstructions() = listOf(
        "xxxxR5R3L2yyy" to "xxxxR8L2yyy",
        "xxxxR2D3L5" to "xxxxR2D3L5",
        "U2R3R5R7D5yyy" to "U2R15D5yyy",
    ).map { (given: String, expected: String) ->
        DynamicTest.dynamicTest(" $given -> $expected") {
            val got = LavaMap.mergeContinuousInstructions(given)
            assertEquals(expected, got)
        }
    }


    @Test
    fun testArea(){
        val given = LavaMap.parse(input.lines(), part=1)

        val got = given.area()

        assertEquals(62L, got)
    }
}