import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Nested

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
            val got = stringMat(matrix).set(2,1, 'Z')

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
                Pair(0,0),
                Pair(0,3),
                Pair(1,1),
                Pair(1,4),
                Pair(2,3),
                Pair(2,4),
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

}