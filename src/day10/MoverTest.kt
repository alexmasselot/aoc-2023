package day10

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class MoverTest {
    @TestFactory
    fun testStep() = listOf(
        ("|" to Mover(listOf(5, 7), listOf(0, 1))) to Mover(listOf(5, 8), listOf(0, 1)),
        ("F" to Mover(listOf(5, 7), listOf(0, -1))) to Mover(listOf(6, 7), listOf(1, 0)),
        ("L" to Mover(listOf(5, 7), listOf(-1, 0))) to Mover(listOf(5,6), listOf(0, -1)),
        ("J" to Mover(listOf(5, 7), listOf(1, 0))) to Mover(listOf(5,6), listOf(0, -1)),
    )
        .map { (input: Pair<String, Mover>, expected: Mover) ->
            DynamicTest.dynamicTest(" $input => $expected") {
                val pipeUnit = PipeUnit.letterSteps[input.first]!!

                val got = input.second.step(pipeUnit)
                assertEquals(expected, got)
            }
        }
}