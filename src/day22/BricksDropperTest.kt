package day22

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BricksDropperTest {
    @Test
    fun dropToGround() {
        val input = """
            0,0,4~0,2,4
            2,0,5~2,2,5
            0,1,6~2,1,6
            1,1,8~1,1,9
        """.trimIndent().lines()
        val bm = BrickMap.parse(input)
        val um = bm.isCatchingMatrix()

        val dropper = BricksDropper(um)
        val got = dropper.dropToGround(bm)

        val inputExpected = """
            0,0,1~0,2,1
            2,0,1~2,2,1
            0,1,6~2,1,6
            1,1,8~1,1,9
        """.trimIndent().lines()
        val expected = BrickMap.parse(inputExpected)
        assertEquals(expected, got)

    }


    @Test
    fun dropThemAll() {
        val input = """
            0,0,4~0,2,4
            2,0,5~2,2,7
            0,1,16~2,1,16
            1,1,18~1,1,19
        """.trimIndent().lines()
        val bm = BrickMap.parse(input)
        val um = bm.isCatchingMatrix()

        val dropper = BricksDropper(um)
        val got = dropper.dropThemAll(bm)
        val inputExpected = """
            0,0,1~0,2,1
            2,0,1~2,2,3
            0,1,4~2,1,4
            1,1,5~1,1,6
        """.trimIndent().lines()
        val expected = BrickMap.parse(inputExpected)
        assertEquals(expected, got)
    }
}