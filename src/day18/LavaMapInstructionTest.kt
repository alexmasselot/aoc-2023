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
    fun testParse1() {
        val given = "R 6 (#70c710)"

        val got = LavaMapInstruction.parse(given, part = 1)

        assertEquals(0 to 1, got.heading.dir)
        assertEquals(Heading.R, got.heading)
        assertEquals(6, got.step)
    }

    @Test
    fun testParse2() {
        val given = "R 6 (#70c710)"

        val got = LavaMapInstruction.parse(given, part = 2)

        assertEquals(0 to 1, got.heading.dir)
        assertEquals(Heading.R, got.heading)
        assertEquals(461937, got.step)

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

    @Test
    fun testPerimeter() {
        val given = LavaMap.parse(input.lines())

        val got = given.perimeter()

        assertEquals(38, got.size)
        assertEquals(0 to 1, got.first())
        assertEquals(0 to 0, got.last())
        assertEquals(1 to 6, got[6])
    }

    @Test
    fun findInnerPosition() {
        val given = LavaMap.parse(input.lines())

        val got = given.findInnerPosition()

        assertEquals(1 to 5, got)
    }

    @Test
    fun testInnerArea() {
        val given = LavaMap.parse(input.lines())

        val got = given.innerArea()

        val expected = """
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

    @Test
    fun testReduceOne1() {
        val given = LavaMap.parse(input.lines())
        val got = given.reduceOne()

        val expected = """
            #####..
            #...#..
            ###.#..
            ..#.#..
            ..#.#..
            ###.#..
            #...#..
            ##..###
            .#....#
            .######
        """.trimIndent()

        assertEquals(expected, got?.first?.perimeterMap()?.toString('#'))
        assertEquals(12L, got?.second)
    }

    @Test
    fun testCollapseContinuousSteps() {
        val given = LavaMap(
            listOf(
                LavaMapInstruction(Heading.L, 1),
                LavaMapInstruction(Heading.D, 2),
                LavaMapInstruction(Heading.R, 3),
                LavaMapInstruction(Heading.R, 4),
                LavaMapInstruction(Heading.U, 5),
            )
        )

        val got = given.cleanUpSteps()
        val expected = LavaMap(
            listOf(
                LavaMapInstruction(Heading.L, 1),
                LavaMapInstruction(Heading.D, 2),
                LavaMapInstruction(Heading.R, 7),
                LavaMapInstruction(Heading.U, 5),
            )
        )

        assertEquals(expected, got)
    }

    @Test
    fun testRotate(){
        val given = LavaMap(
            listOf(
                LavaMapInstruction(Heading.L, 1),
                LavaMapInstruction(Heading.D, 2),
                LavaMapInstruction(Heading.R, 7),
                LavaMapInstruction(Heading.U, 5),
            )
        )

        val got = given.rotate()

        val expected =
            LavaMap(
                listOf(
                    LavaMapInstruction(Heading.U, 1),
                    LavaMapInstruction(Heading.L, 2),
                    LavaMapInstruction(Heading.D, 7),
                    LavaMapInstruction(Heading.R, 5),
                )
            )

        assertEquals(expected, got)
    }


    @Test
    fun testMirror() {
        val given = LavaMap.parse(input.lines())

        val got = given.mirror()

        val expected = """
            #######
            #.....#
            #...###
            #...#..
            #...#..
            ###.###
            ..#...#
            ###..##
            #....#.
            ######.
        """.trimIndent()

        assertEquals(expected, got.perimeterMap().toString('#'))
    }


    @Test
    fun testGrittyArea(){
        val given = LavaMap.parse(input.lines(), part=1)

        val got = given.grittyArea()

        assertEquals(62L, got)
    }
}