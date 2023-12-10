package day10

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class MoverTest {
    @TestFactory
    fun testStep() = listOf(
        ('|' to Mover(listOf( 7, 5), listOf(  1,  0))) to Mover(listOf(8, 5 ), listOf( 1,0 )),
        ('F' to Mover(listOf( 7, 5), listOf( -1,  0))) to Mover(listOf(7, 6 ), listOf( 0,1 )),
        ('L' to Mover(listOf( 7, 5), listOf(  0, -1))) to Mover(listOf(6, 5 ), listOf(-1,0 )),
        ('J' to Mover(listOf( 7, 5), listOf(  0,  1))) to Mover(listOf(6, 5 ), listOf(-1,0 )),
    )
        .map { (input: Pair<Char, Mover>, expected: Mover) ->
            DynamicTest.dynamicTest(" $input => $expected") {
                val pipeUnit = PipeUnit.letterSteps[input.first]!!

                val got = input.second.step(pipeUnit)
                assertEquals(expected, got)
            }
        }
}