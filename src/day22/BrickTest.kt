package day22

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class BrickTest {
    val input = """
        1,0,1~1,2,1
        0,0,2~2,0,2
        0,2,3~2,2,3
        0,0,4~0,2,4
        2,0,5~2,2,5
        0,1,6~2,1,6
        1,1,8~1,1,9
    """.trimIndent().lines()

    fun inputBricks() = input.map { Brick.parse(it) }

    @Test
    fun `parse brick`() {
        val brick = Brick.parse("1,2,3~4,5,6")
        assertEquals(1, brick.p1.x)
        assertEquals(2, brick.p1.y)
        assertEquals(3, brick.p1.z)
        assertEquals(4, brick.p2.x)
        assertEquals(5, brick.p2.y)
        assertEquals(6, brick.p2.z)
    }

    @TestFactory
    fun isBellow() = listOf(
        "A" to listOf("B", "C", "F", "G"),
        "B" to listOf("D", "E"),
        "C" to listOf("D", "E"),
        "D" to listOf("F"),
        "E" to listOf("F"),
        "F" to listOf("G"),
        "G" to listOf(),
    ).flatMap { (x, ys) ->
        ys.map { y -> (x to y) to true }
            .plus(ys.map { y -> (y to x) to false })
            .plus("ABCDEFG".toList().map { it.toString() }.filter { !ys.contains(it) && it != x }.map { (x to it) to false })
    }.map { (input: Pair<String, String>, expected: Boolean) ->
        DynamicTest.dynamicTest(" ${input.first} support ${input.second}: $expected") {
            val given = inputBricks()
            val i1 = input.first.first().code - 'A'.code
            val i2 = input.second.first().code - 'A'.code
            val got = given[i1].isBellow(given[i2])

            assertEquals(expected, got)
        }
    }

    @Test
    fun `falling brick`() {
        val given = inputBricks()
        val got = given[0].isBellow(given[1])
        assertTrue(got)
    }
}