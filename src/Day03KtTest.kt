import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class Day03KtTest {

    @Test
    fun testNumberSpread() {
        val given = "467..114.."
        val got = numberSpread(given)

        assertEquals(listOf(467, 467, 467, null, null, 114, 114, 114, null, null), got)
    }
}