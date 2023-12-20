package day18

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import toString

class LavaMapInstructionTest {
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
    fun testParse() {
        val given = "R 6 (#70c710)"

        val got = LavaMapInstruction.parse(given)

        assertEquals(0 to 1, got.direction)
        assertEquals(6, got.step)
        assertEquals(0x70c710, got.color)
    }

    @Test
    fun testFindBoundariesMax() {
        val map = LavaMapInstructionList.parse(input.lines())

        val got = map.findBoundariesMax()

        assertEquals(9 to 6, got)
    }
    @Test
    fun testFindBoundariesMin() {
        val map = LavaMapInstructionList.parse(input.lines())

        val got = map.findBoundariesMin()

        assertEquals(0 to 0, got)
    }

    @Test
    fun testPerimeterMap(){
        val given = LavaMapInstructionList.parse(input.lines())

        val got = given.perimeterMap()

        val expected ="""
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
    fun testIsTurningRight(){
        val given = LavaMapInstructionList.parse(input.lines())

        val got = given.isTurningRight()

        assertTrue(got)
    }

    @Test
    fun testPerimeter(){
        val given = LavaMapInstructionList.parse(input.lines())

        val got = given.perimeter()

        assertEquals(38, got.size)
        assertEquals(0 to 1, got.first())
        assertEquals(0 to 0, got.last())
        assertEquals(1 to 6, got[6])
    }

    @Test
    fun findInnerPosition(){
        val given = LavaMapInstructionList.parse(input.lines())

        val got = given.findInnerPosition()

        assertEquals(1 to 5, got)
    }

    @Test
    fun testInnerArea(){
        val given = LavaMapInstructionList.parse(input.lines())

        val got = given.innerArea()

        val expected ="""
            #######
            #######
            #######
            ..#####
            ..#####
            #######
            #####..
            #######
            .######
            .######
        """.trimIndent()

        assertEquals(expected, got.toString('#'))
    }
}