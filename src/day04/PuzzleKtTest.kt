package day04

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class PuzzleKtTest {

    @Test
    fun parseGame04() {
        val given = "Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53"
        val got = parseGame04(given)

        assertEquals(setOf(41, 48, 83, 86, 17), got.first)
        assertEquals(setOf(83, 86,  6, 31, 17,  9, 48, 53), got.second)
    }

    @Test
    fun testPileCards04(){
        val given = listOf(4, 2, 2, 1, 0, 0)

        val got = pileCards04(given)
        val expected = listOf(1,2,4,8,14,1)

        assertEquals(
            expected,
            got
        )
    }
}