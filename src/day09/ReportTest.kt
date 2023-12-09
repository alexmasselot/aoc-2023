package day09

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class ReportTest {
    @TestFactory
    fun testLastDeltas() = listOf(
        listOf(0, 3, 6, 9, 12, 15) to listOf(3 to 3),
        listOf(1, 3, 6, 10, 15, 21) to listOf(2 to 6, 1 to 1)
    )
        .map { (input, expected) ->
            DynamicTest.dynamicTest(" $input => lastDeltas: $expected") {
                val got = Report.firstLastDeltas(input)
                assertEquals(expected, got)
            }
        }

    @TestFactory
    fun testNext() = listOf(
        listOf(0, 3, 6, 9, 12, 15) to 18,
        listOf(1, 3, 6, 10, 15, 21) to 28,
        listOf(10, 13, 16, 21, 30, 45) to 68
    )
        .map { (input, expected) ->
            DynamicTest.dynamicTest(" $input => next: $expected") {
                val got = Report.next(input)
                assertEquals(expected, got)
            }
        }

    @TestFactory
    fun testPrevious() = listOf(
        listOf(0, 3, 6, 9, 12, 15) to -3,
        listOf(1, 3, 6, 10, 15, 21) to 0,
        listOf(10, 13, 16, 21, 30, 45) to 5
    )
        .map { (input, expected) ->
            DynamicTest.dynamicTest(" $input => previous: $expected") {
                val got = Report.previous(input)
                assertEquals(expected, got)
            }
        }

}