import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CellularAutomataTest {
    val rules = listOf(
        0,
        1,
        2,
        3,
        4,
        10,
        6,
        7,
        8,
        9,
        5,
        11,
        12,
        13,
        14,
        15,
        // bouncing on blocks
        16 or 0,    // 0,
        16 or 4,    // 1,
        16 or 8,    // 2,
        16 or 12,   // 3,
        16 or 1,    // 4,
        16 or 10,    // 5,
        16 or 9,    // 6,
        16 or 13,    // 7,
        16 or 2,    // 8,
        16 or 6,    // 9,
        16 or 5,    // 10,
        16 or 14,    // 11,
        16 or 3,    // 12,
        16 or 7,    // 13,
        16 or 11,    // 14,
        16 or 15,    // 15,
    )
    val step0 = listOf(
        listOf(0, 0, 0, 0),
        listOf(0, 8, 0, 2),
        listOf(16 or 0, 5, 0, 0),
    )
    val step1 = listOf(
        listOf(0, 0, 0, 0),
        listOf(0, 0, 10, 0),
        listOf(16 or 2, 0, 8, 0),
    )
    val step2 = listOf(
        listOf(0, 0, 1, 0),
        listOf(0, 0, 0, 0),
        listOf(16 or 0, 8, 4, 8),
    )
    val step3 = listOf(
        listOf(0, 0, 0, 0),
        listOf(0, 0, 0, 0),
        listOf(16 or 0, 0, 8, 0),
    )

    @Test
    fun testStep1() {
        val given = CellularAutomata(
            step0,
            rules
        )

        val got = given.next()

        assertEquals(step1, got.grid)
    }

    @Test
    fun testStep2() {
        val given = CellularAutomata(
            step1,
            rules
        )

        val got = given.next()

        assertEquals(step2, got.grid)
    }

    @Test
    fun testStep3() {
        val given = CellularAutomata(
            step2,
            rules
        )

        val got = given.next()

        assertEquals(step3, got.grid)
    }
}