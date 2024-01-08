import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class UtilsKtTest {
    @Test
    fun testMapToBooleanList() {
        val given = """
            123%.=56
            98+..&9+
        """.trimIndent()
            .split("\n")

        val got = given.mapToBooleanList { c -> c.isDigit() || c == '.' }
        val expected = listOf(
            listOf(true, true, true, false, true, false, true, true),
            listOf(true, true, false, true, true, false, true, false)
        )

        assertEquals(expected, got)
    }

    @Nested
    inner class MatrixOperations {
        fun stringToBool(input: String) =
            input.split("\n")
                .mapToBooleanList { it == 'X' }

        fun stringMat(input: String): List<List<Char>> =
            input.split("\n")
                .map { it.toList() }

        val matrix = """
        X..X.
        .X..X
        ...XX
        """.trimIndent()


        @Test
        fun testSet() {
            val got = stringMat(matrix).set(2, 1, 'Z')

            val exp = stringMat(
                """
                X..X.
                .X..X
                .Z.XX
            """.trimIndent()
            )

            assertEquals(exp, got)
        }

        @Test
        fun testFindMatches() {
            val got = stringMat(matrix).findIndexes { it == 'X' }

            val expected = listOf(
                Pair(0, 0),
                Pair(0, 3),
                Pair(1, 1),
                Pair(1, 4),
                Pair(2, 3),
                Pair(2, 4),
            )
            assertEquals(expected, got)
        }

        @Test
        fun testShiftE() {
            val got = stringToBool(matrix).shiftE(true)

            val exp = stringToBool(
                """
                XX..X
                X.X..
                X...X
            """.trimIndent()
            )

            assertEquals(exp, got)
        }

        @Test
        fun testShiftW() {
            val got = stringToBool(matrix).shiftW(true)

            val exp = stringToBool(
                """
                ..X.X
                X..XX
                ..XXX
            """.trimIndent()
            )
            assertEquals(exp, got)
        }

        @Test
        fun testShiftN() {
            val got = stringToBool(matrix).shiftN(true)

            val exp = stringToBool(
                """
                .X..X
                ...XX
                XXXXX
            """.trimIndent()
            )
            assertEquals(exp, got)
        }

        @Test
        fun testShiftS() {
            val got = stringToBool(matrix).shiftS(false)

            val exp = stringToBool(
                """
                .....
                X..X.
                .X..X
            """.trimIndent()
            )
            assertEquals(exp, got)
        }

        @Test
        fun testOr() {
            val given1 = stringToBool(
                """
                X..X
                .X.X
            """.trimIndent()
            )
            val given2 = stringToBool(
                """
                .X.X
                X..X
            """.trimIndent()
            )

            val got = given1.or(given2)

            val exp = stringToBool(
                """
                XX.X
                XX.X
            """.trimIndent()
            )
            assertEquals(exp, got)
        }

        @Test
        fun testAnd() {
            val given1 = stringToBool(
                """
                X..X
                .X.X
            """.trimIndent()
            )
            val given2 = stringToBool(
                """
                .X.X
                X..X
            """.trimIndent()
            )

            val got = given1.and(given2)

            val exp = stringToBool(
                """
                ...X
                ...X
            """.trimIndent()
            )
            assertEquals(exp, got)
        }
    }

    @Test
    fun testRotateClockwise() {
        val given = listOf(
            listOf(0, 1, 2),
            listOf(3, 4, 5),
            listOf(6, 7, 8),
        )

        val expected = listOf(
            listOf(6, 3, 0),
            listOf(7, 4, 1),
            listOf(8, 5, 2)
        )

        val got = given.rotateClockwise()
        assertEquals(expected, got)
    }

    @Test
    fun testFindLCM1() {
        val given = listOf(3L, 5L, 7L)
        val got = findLCM(given)

        assertEquals(3 * 5 * 7, got)
    }

    @Test
    fun testFindLCM2() {
        val given = listOf(3L * 5, 5L * 3, 7L * 5 * 7)
        val got = findLCM(given)

        assertEquals(3 * 5 * 7 * 7, got)
    }

    @Test
    fun toPrimeFactors() {
        val given = 204L
        val got = toPrimeFactors(given)
        val expected = listOf(2L, 2L, 3L, 17L)
        assertEquals(expected, got)
    }
}