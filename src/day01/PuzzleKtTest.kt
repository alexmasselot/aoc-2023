package day01

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PuzzleKtTest {
    @Test
    fun `test xa1dfs3sdf5`() {

        val got = extractDigits("xa1dfs3sdf5")
        val expected = listOf(1, 3, 5)

        assertEquals(expected, got)
    }

    @Test
    fun `test sevenxxxeighthreetwo`() {

        val got = extractDigits("sevenxxxeighthreetwo")
        val expected = listOf(7, 8, 3, 2)

        assertEquals(expected, got)
    }
    @Test
    fun `test sevenxxxeightthreetwo`() {

        val got = extractDigits("sevenxxxeightthreetwo")
        val expected = listOf(7, 8, 3, 2)

        assertEquals(expected, got)
    }
}