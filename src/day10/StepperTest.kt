package day10

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class StepperTest {

    @TestFactory
    fun testSteer() = listOf(
        ('|' to listOf(1, 0)) to listOf(1, 0),
        ('|' to listOf(-1, 0)) to listOf(-1, 0),
        ('-' to listOf(0, 1)) to listOf(0, 1),
        ('-' to listOf(0, -1)) to listOf(0, -1),
        ('7' to listOf(0, 1)) to listOf(1, 0),
        ('7' to listOf(-1, 0)) to listOf(0, -1),
        ('L' to listOf(1, 0)) to listOf(0, 1),
        ('L' to listOf(0, -1)) to listOf(-1, 0),
        ('F' to listOf(-1, 0)) to listOf(0, 1),
        ('F' to listOf(0, -1)) to listOf(1, 0),
        ('J' to listOf(0, 1)) to listOf(-1, 0),
        ('J' to listOf(1, 0)) to listOf(0, -1),
    )
        .map { (input: Pair<Char, List<Int>>, expected: List<Int>) ->
            DynamicTest.dynamicTest(" $input => steer: $expected") {
                val pipeUnit = PipeUnit.letterSteps[input.first]!!

                val got = pipeUnit.steer(input.second)
                assertEquals(expected, got)
            }
        }
}