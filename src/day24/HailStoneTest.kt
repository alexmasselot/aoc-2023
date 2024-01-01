package day24

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class HailStoneTest {
    @Test
    fun `parse input`() {
        val input = "19, 13, 30 @ -2,  1, -2"
        val expected = HailStone(Position(19, 13, 30), Velocity(-2, 1, -2))
        assertEquals(expected, HailStone.parse(input))
    }

    @Test
    fun trajectoryIntersect2D(){
        val h1 = HailStone.parse("19, 13, 30 @ -2, 1, -2")
        val h2 = HailStone.parse("18, 19, 22 @ -1, -1, -2")
        val got = h1.trajectoryIntersect2DInFuture(h2)
        assertNotNull(got)
        assertTrue(got!!.first > 14.333)
        assertTrue(got!!.first < 14.334)
        assertTrue(got!!.second < 15.334)
        assertTrue(got!!.second > 15.333)
    }


    @Test
    fun `trajectoryIntersect2D does not intersect`(){
        val h1 = HailStone.parse("19, 13, 30 @ -2, -1, -2")
        val h2 = HailStone.parse("18, 19, 22 @ 4, 2, -2")
        val got = h1.trajectoryIntersect2DInFuture(h2)
        assertNull(got)
    }

    @Test
    fun `trajectoryIntersect2D intersect in past`(){
        val h1 = HailStone.parse("20, 25, 34 @ -2, -2, -4")
        val h2 = HailStone.parse("120, 19, 15 @ 1, -5, -3")
        val got = h1.trajectoryIntersect2DInFuture(h2)
        assertNull(got)
    }

}