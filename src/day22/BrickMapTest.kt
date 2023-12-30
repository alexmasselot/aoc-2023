package day22

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.BitSet

class BrickMapTest{
    val input ="""
        1,0,1~1,2,1
        0,0,2~2,0,2
        0,2,3~2,2,3
        0,0,4~0,2,4
        2,0,5~2,2,5
        0,1,6~2,1,6
        1,1,8~1,1,9
    """.trimIndent().lines()

    fun inputBricks() = BrickMap.parse(input)

    @Test
    fun isBelowMatrix(){
        val given = inputBricks()
        val got = given.isBellowMatrix()
        assertEquals(7, got.size)
        assertEquals(BitSet.valueOf(longArrayOf(0b1100110)), got.row(0))
        assertEquals(BitSet.valueOf(longArrayOf(0b0011000)), got.row(1))
        assertEquals(BitSet.valueOf(longArrayOf(0b0011000)), got.row(2))
        assertEquals(BitSet.valueOf(longArrayOf(0b0100000)), got.row(3))
        assertEquals(BitSet.valueOf(longArrayOf(0b0100000)), got.row(4))
        assertEquals(BitSet.valueOf(longArrayOf(0b1000000)), got.row(5))
        assertEquals(BitSet.valueOf(longArrayOf(0b0000000)), got.row(6))
    }

    @Test
    fun `multiply once bitMatrix`(){
        val given = inputBricks().isBellowMatrix()
        val got = given.multiply(given)

        assertEquals(BitSet.valueOf(longArrayOf(0b1011000)), got.row(0))
        assertEquals(BitSet.valueOf(longArrayOf(0b0100000)), got.row(1))
        assertEquals(BitSet.valueOf(longArrayOf(0b0100000)), got.row(2))
        assertEquals(BitSet.valueOf(longArrayOf(0b1000000)), got.row(3))
        assertEquals(BitSet.valueOf(longArrayOf(0b1000000)), got.row(4))
        assertEquals(BitSet.valueOf(longArrayOf(0b0000000)), got.row(5))
        assertEquals(BitSet.valueOf(longArrayOf(0b0000000)), got.row(6))
    }
    @Test
    fun `isCatchingMatrix`(){
        val given = inputBricks()
        val got = given.isCatchingMatrix()
        assertEquals(BitSet.valueOf(longArrayOf(0b0000110)), got.row(0))
        assertEquals(BitSet.valueOf(longArrayOf(0b0011000)), got.row(1))
        assertEquals(BitSet.valueOf(longArrayOf(0b0011000)), got.row(2))
        assertEquals(BitSet.valueOf(longArrayOf(0b0100000)), got.row(3))
        assertEquals(BitSet.valueOf(longArrayOf(0b0100000)), got.row(4))
        assertEquals(BitSet.valueOf(longArrayOf(0b1000000)), got.row(5))
        assertEquals(BitSet.valueOf(longArrayOf(0b0000000)), got.row(6))
    }

    @Test
    fun `countMonoSupports`(){
        val given = inputBricks()
        val got = given.countMonoSupports()
        assertEquals(2, got)
    }

    @Test
    fun `countMonoSupports suffled`(){
        val given = BrickMap(inputBricks().bricks.reversed())
        val got = given.countMonoSupports()
        assertEquals(2, got)
    }

}