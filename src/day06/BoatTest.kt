package day06

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class BoatTest {

    @Test
    fun moveForTime3() {
        val boat = Boat(7L, 9L)
        assertEquals(12L, boat.moveForTime(3L))
    }

    @Test
    fun moveForTime2() {
        val boat = Boat(7L, 9L)
        assertEquals(10L, boat.moveForTime(2L))
    }

    @Test
    fun pressBeatRecord() {
        val boat = Boat(7L, 9L)
        assertEquals(listOf(2L, 3L, 4L, 5L), boat.pressBeatRecord())
    }

    @Test
    fun countPressBeatRecord() {
        val boat = Boat(7L, 9L)
        assertEquals(4L, boat.countPressBeatRecord())
    }

    @Test
    fun readBoats() {
        val given = """
            Time:      7  15   30
            Distance:  9  40  200
        """.trimIndent().split("\n")
        val got = Boat.readBoats(given)

        assertEquals(
            listOf(
                Boat(7L, 9L),
                Boat(15L, 40L),
                Boat(30L, 200L),
            ), got
        )
    }
}