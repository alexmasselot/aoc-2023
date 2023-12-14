package day13

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import println

class PatternTest {
    val input1 = """
            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#
        """.trimIndent()

    val input2 = """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.
        """.trimIndent()

    @Test
    fun testParseLine() {
        val given = "#..#."
        val got = Pattern.parseLine(given)

        assertEquals("10010".toInt(2), got)
    }

    @Test
    fun testTranspose() {
        val given = Pattern(listOf(3, 4, 1), 4)
        val got = given.transpose()
        val expected = Pattern(listOf(0, 2, 4, 5), 3)
        assertEquals(expected.map, got.map)
        assertEquals(expected.nbCols, got.nbCols)
    }

    @Test
    fun testisRowMirrored() {
        val given = Pattern.parse(input1.lines())
        assertFalse(given.isRowMirrored(1))
        assertFalse(given.isRowMirrored(2))
        assertFalse(given.isRowMirrored(3))
        assertTrue(given.isRowMirrored(4))
        assertFalse(given.isRowMirrored(5))
        assertFalse(given.isRowMirrored(6))
        assertFalse(given.isRowMirrored(7))
    }

    @Test
    fun testIsRowMirroredLastsLineIncluded() {
        val input = """
            ##.##.#
            ...##..
            ..####.
            ..####.
            #..##..
            ##....#
            ..####.
            ..####.
            ###..##
        """.trimIndent()
        val pattern = Pattern.parse(input.lines())
        assertFalse(pattern.isRowMirrored(7))
    }

    @Test
    fun testisColMirroredLastRow() {
        val input = """
            ....#.....#.##.#.
            #..###.#.##.##.#.
            ....#.#.##.......
            ##.#..#.###..##..
            ..##.....#.#....#
            ..####.#.#####.#.
            ..####.#.#####.#.
            ..##.....#.#.....
            ##.#..#.###..##..
            ....#.#.##.......
            #..###.#.##.##.#.
            ....#.....#.##.#.
            ###..#..#.....##.
            #..#.#.#...#####.
            #..#.#.#...#####.
        """.trimIndent()
        val pattern = Pattern.parse(input.lines())

        assertTrue(pattern.isRowMirrored(14))
    }

    @Test
    fun testIsColMirrored() {
        val pattern = Pattern.parse(input2.lines())
        assertFalse(pattern.isColMirrored(0))
        assertFalse(pattern.isColMirrored(1))
        assertFalse(pattern.isColMirrored(2))

        assertFalse(pattern.isColMirrored(3))
        assertFalse(pattern.isColMirrored(4))
        assertTrue(pattern.isColMirrored(5))
        assertFalse(pattern.isColMirrored(6))
        assertFalse(pattern.isColMirrored(7))
        assertFalse(pattern.isColMirrored(8))
        assertFalse(pattern.isColMirrored(9))
    }

    @Test
    fun testFindAllMirrorRows1() {
        val pattern = Pattern.parse(input1.lines())
        val got = pattern.findAllMirrorRows()
        val expected = listOf(4)
        assertEquals(expected, got)
    }

    @Test
    fun testFindAllMirrorRows2() {
        val pattern = Pattern.parse(input2.lines())
        val got = pattern.findAllMirrorRows()
        val expected = emptyList<Int>()
        assertEquals(expected, got)
    }

    @Test
    fun testFindAllMirrorCols1() {
        val pattern = Pattern.parse(input1.lines())

        val got = pattern.findAllMirrorCols()
        val expected = emptyList<Int>()
        assertEquals(expected, got)
    }

    @Test
    fun testFindAllMirrorCols2() {
        val pattern = Pattern.parse(input2.lines())
        val got = pattern.findAllMirrorCols()
        val expected = listOf(5)
        assertEquals(expected, got)
    }

    @TestFactory
    fun testBinaryDistance() = listOf(
        Triple("1000101", "1000101", 0) to 0,
        Triple("1000101", "1010101", 0) to null,
        Triple("000100", "000101", 1) to 1,
        Triple("000100", "100101", 1) to null,
        Triple("000101", "000100", 1) to null,
    ).map { (input: Triple<String, String, Int>, expected: Int?) ->
        DynamicTest.dynamicTest(" $input => $expected") {
            val got = Pattern.binaryAdd(input.first.toInt(2), input.second.toInt(2), input.third)
            assertEquals(expected, got)
        }
    }

    @Test
    fun testisRowMirroredSmudge1() {
        val input = """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.
        """.trimIndent()
        val given = Pattern.parse(input.lines())
        assertFalse(given.isRowMirrored(1, 1))
        assertFalse(given.isRowMirrored(2, 1))
        assertTrue(given.isRowMirrored(3, 1))
        assertFalse(given.isRowMirrored(4, 1))
        assertFalse(given.isRowMirrored(5, 1))
        assertFalse(given.isRowMirrored(6, 1))
        assertFalse(given.isRowMirrored(7, 1))
    }


}