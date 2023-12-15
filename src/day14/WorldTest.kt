package day14

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class WorldTest{
    val input ="""
            O....#....
            O.OO#....#
            .....##...
            OO.#O....O
            .O.....O#.
            O.#..O.#.#
            ..O..#O..O
            .......O..
            #....###..
            #OO..#....
        """.trimIndent()
    @Test
    fun testParse(){
        val input = """
            .#.
            O#.
            .#O
        """.trimIndent()
        val expected = World(
            listOf(
                listOf(false, true, false),
                listOf(false, true, false),
                listOf(false, true, false)
            ),
            listOf(
                listOf(false, false, false),
                listOf(true, false, false),
                listOf(false, false, true)
            ),
            listOf(
                listOf(true, false, true),
                listOf(false, false, true),
                listOf(true, false, false)
            )
        )
        val got = World.parse(input.lines())
        assertEquals(expected, got)

    }

    @Test
    fun testToString(){
        val input = """
            .#.
            O#.
            .#O
        """.trimIndent()
        val expected = """
            .#.
            O#.
            .#O
        """.trimIndent()
        val got = World.parse(input.lines()).toString()
        assertEquals(expected, got)
    }

    @Test
    fun testRollNorth1(){
        val input = """
            .##
            O#.
            .OO
        """.trimIndent()
        val got = World.parse(input.lines()).rollNorth()
        val expected = """
            O##
            .#O
            .O.
        """.trimIndent()
        assertEquals(expected, got.toString())
    }

    @Test
    fun testRollNorth2(){
        val input = """
           O....#....
           O.OO#....#
           .....##...
           OO.#O....O
           .O.....O#.
           O.#..O.#.#
           ..O..#O..O
           .......O..
           #....###..
           #OO..#....
        """.trimIndent()
        val got = World.parse(input.lines()).rollNorth()
        val expected = """
            OOOO.#.O..
            OO..#....#
            OO..O##..O
            O..#.OO...
            ........#.
            ..#....#.#
            ..O..#.O.O
            ..O.......
            #....###..
            #....#....
        """.trimIndent()
        assertEquals(expected, got.toString())
    }

    @Test
    fun testLoad(){
        val input ="""
            OOOO.#.O..
            OO..#....#
            OO..O##..O
            O..#.OO...
            ........#.
            ..#....#.#
            ..O..#.O.O
            ..O.......
            #....###..
            #....#....
        """.trimIndent()
        val got = World.parse(input.lines()).load()

        assertEquals(136, got)
    }

    @Test
    fun testTurnRight(){
        val input = """
            .##
            O#.
            .OO
        """.trimIndent()
        val got = World.parse(input.lines()).rotateClockWise()

        val expected="""
            .O.
            O##
            O.#
        """.trimIndent()

        assertEquals(expected, got.toString())
    }
    @Test
    fun testTilt(){
        val world = World.parse(input.lines())

        val got = world.tilt()

        val expected = """
            .....#....
            ....#...O#
            ...OO##...
            .OO#......
            .....OOO#.
            .O#...O#.#
            ....O#....
            ......OOOO
            #...O###..
            #..OO#....
        """.trimIndent()

        assertEquals(expected, got.toString())
    }

    @Test
    fun testFindLoop(){
        val given = World.parse(input.lines())

        val got = given.findLoop()

        assertEquals(10 to 3, got)
    }

    @Test
    fun testTiltN(){
        val given = World.parse(input.lines())

        val got = given.tiltN(1000000000L).load()

        assertEquals(64, got)
    }
}