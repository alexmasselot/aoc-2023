package day10

import countIf
import mapMatrix
import mask
import not
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import println
import set
import toString
import toStringMat
import zipApply

class PipeWorldTest {
    val map = """
        -|L|F7
        7|S-7|
        L||7||
        -|L-J|
        L||-JF
    """.trimIndent().split("\n")

    @Test
    fun testParseInput() {
        val pw = PipeWorld.fromInput(map)

        assertEquals(listOf(1, 2), pw.start)
    }

    @Test
    fun testPossibleStarts1() {
        val pw = PipeWorld.fromInput(map)
        val got = pw.possibleStarts()

        assertEquals(
            listOf(
                Mover(listOf(1, 3), listOf(0, 1)),
                Mover(listOf(2, 2), listOf(1, 0)),
            ), got
        )
    }

    @Test
    fun testPossibleStarts2() {
        val map = """
            ..F7.
            .FJ|.
            SJ.L7
            |F--J
            LJ...
        """.trimIndent().split("\n")
        val pw = PipeWorld.fromInput(map)
        val got = pw.possibleStarts()

        assertEquals(
            listOf(
                Mover(listOf(2, 1), listOf(0, 1)),
                Mover(listOf(3, 0), listOf(1, 0)),
            ), got
        )
    }

    @Test
    fun testFurthestPath1() {
        val given = """
            .....
            .S-7.
            .|.|.
            .L-J.
            .....
        """.trimIndent().split("\n")
        val pw = PipeWorld.fromInput(given)

        val got = pw.furthestPath()

        assertEquals(4, got)
    }

    @Test
    fun testFurthestPath2() {
        val given = """
            ..F7.
            .FJ|.
            SJ.L7
            |F--J
            LJ...
        """.trimIndent().split("\n")
        val pw = PipeWorld.fromInput(given)

        val got = pw.furthestPath()

        assertEquals(8, got)
    }

    @Test
    fun testTurningTotal() {
        val given = """
            .....
            .S-7.
            .|.|.
            .L-J.
            .....
        """.trimIndent().split("\n")
        val pw = PipeWorld.fromInput(given)

        val got = pw.turningTotal()

        assertEquals(3, got)
    }

    @Test

    fun testTurningTotalReverse() {
        val given = """
            .....
            .S-7.
            .|.|.
            .L-J.
            .....
        """.trimIndent().split("\n")
        val pw = PipeWorld.fromInput(given)

        val got = pw.turningTotal(true)

        assertEquals(-3, got)
    }

    @Test
    fun testTFenceMap() {
        val given = """
            .....
            .S-7.
            .|F|L
            .L-J.
            ..||.
        """.trimIndent().split("\n")
        val pw = PipeWorld.fromInput(given)

        val got = pw.fenceMap()

        val expected = """
            .....
            .XXX.
            .X.X.
            .XXX.
            .....
        """.trimIndent()
        assertEquals(expected, got.toString('X'))
    }

    @Test
    fun testPaintInside() {
        val given = """
        ...........
        .S-------7.
        .|F-----7|.
        .||.....||.
        .||.....||.
        .|L-7.F-J|.
        .|..|.|..|.
        .L--J.L--J.
        ...........""".trimIndent().split("\n")
        val pw = PipeWorld.fromInput(given)

        val got = pw.paintInside()
        val gotMap = given.map { it.toList() }.mask(got.not(), 'I').toStringMat()

        val expected = """
            ...........
            .S-------7.
            .|F-----7|.
            .||.....||.
            .||.....||.
            .|L-7.F-J|.
            .|II|.|II|.
            .L--J.L--J.
            ...........
        """.trimIndent()

        assertEquals(expected, gotMap)
    }

    @Test
    fun testPaintInside2() {
        val given = """
        FF7FSF7F7F7F7F7F---7
        L|LJ||||||||||||F--J
        FL-7LJLJ||||||LJL-77
        F--JF--7||LJLJ7F7FJ-
        L---JF-JLJ.||-FJLJJ7
        |F|F-JF---7F7-L7L|7|
        |FFJF7L7F-JF7|JL---7
        7-L-JL7||F7|L7F-7F7|
        L.L7LFJ|||||FJL7||LJ
        L7JLJL-JLJLJL--JLJ.L
        """.trimIndent().split("\n")
        val pw = PipeWorld.fromInput(given)

        val got = pw.paintInside()
        val gotMap = pw.pipes.mapMatrix { it?.symbol ?: '.' }
            .set(pw.start[0], pw.start[1], 'S')
            .zipApply(got) { m, inside ->
                if (inside) 'I' else m
            }
            .toStringMat()

        val expected = """
            FF7FSF7F7F7F7F7F---7
            L|LJ||||||||||||F--J
            FL-7LJLJ||||||LJL-77
            F--JF--7||LJLJIF7FJ-
            L---JF-JLJIIIIFJLJJ7
            |F|F-JF---7IIIL7L|7|
            |FFJF7L7F-JF7IIL---7
            7-L-JL7||F7|L7F-7F7|
            L.L7LFJ|||||FJL7||LJ
            L7JLJL-JLJLJL--JLJ.L
        """.trimIndent()

        assertEquals(expected, gotMap)
        assertEquals(10, got.countIf { it })
    }
}