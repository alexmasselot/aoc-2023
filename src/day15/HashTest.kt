package day15

import day13.Pattern
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class HashTest {
    @TestFactory

    fun testHashChar() = listOf(
        ('H' to 0) to 200,
        ('A' to 200) to 153,
        ('S' to 153) to 172,
        ('H' to 172) to 52,
    ).map { (input: Pair<Char, Int>, expected: Int) ->
        DynamicTest.dynamicTest(" $input => $expected") {
            val got = Hash.hashChar(input.first, input.second)
            assertEquals(expected, got)
        }
    }

    @TestFactory
    fun testHash() = listOf(
        "rn=1" to 30,
        "cm-" to 253,
        "qp=3" to 97,
    ).map { (input: String, expected: Int) ->
        DynamicTest.dynamicTest(" $input => $expected") {
            val got = Hash.hash(input)
            assertEquals(expected, got)
        }
    }
}