package day05

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class PosMappingTest {
    val pmList = PosMappingList(
        listOf(
            PosMapping(50, 98, 2),
            PosMapping(52, 50, 48),
        )
    )

    @Nested
    inner class Map {
        @Test
        fun testTo() {
            val pm = PosMapping(50, 98, 2)

            assertEquals(50L, pm.to(98))
            assertEquals(51L, pm.to(99))
            assertEquals(97L, pm.to(97))
            assertEquals(100L, pm.to(100))
        }
        @Test
        fun testIsMoved() {
            val pm = PosMapping(50, 98, 2)

            assertEquals(true,  pm.isMoved(98))
            assertEquals(true,  pm.isMoved(99))
            assertEquals(false, pm.isMoved(97))
            assertEquals(false, pm.isMoved(100))
        }

        @Test
        fun testToList(){
            assertEquals(50L, pmList.to(98))
            assertEquals(55L, pmList.to(53))
            assertEquals(0, pmList.to(0))
        }
    }

    @Test
    fun testFromString() {
        val given = """
           50 98 2
           52 50 48
        """.trimIndent()

        val got = PosMappingList.fromString(given)

        assertEquals(
            got,
            pmList
        )


    }

}