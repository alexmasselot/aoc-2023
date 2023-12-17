package day16

import CellularAutomata
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class BeamCATest{
    @Test
    fun testRuleDot(){
        assertEquals((0..15).toList(), BeamCA.rules.take(16))
    }

    @TestFactory
    fun testRuleSplitterMinus() = listOf(
        0 to 0,
        1 to 10,
        2 to 2,
        3 to 10,
        4 to 10,
        5 to 10,
        6 to 10,
        7 to 10,
        8 to 8,
        9 to 10,
        10 to 10,
        11 to 10,
        12 to 10,
        13 to 10,
        14 to 10,
        15 to 10,
    ).map { (beam: Int, expected: Int) ->
        DynamicTest.dynamicTest(" $beam => $expected") {
            val brick = 1
            val iRule = (brick shl 4) or beam
            val got = BeamCA.rules[(brick shl 4) or beam]
            assertEquals(iRule and CellularAutomata.freezedBits, got and CellularAutomata.freezedBits)
            assertEquals( expected, got and CellularAutomata.shiftedBits)
        }
    }

 @TestFactory
    fun testRuleSplitterPipe() = listOf(
        0 to 0,
        1 to 1,
        2 to 5,
        3 to 5,
        4 to 4,
        5 to 5,
        6 to 5,
        7 to 5,
        8 to 5,
        9 to 5,
        10 to 5,
        11 to 5,
        12 to 5,
        13 to 5,
        14 to 5,
        15 to 5,
    ).map { (beam: Int, expected: Int) ->
        DynamicTest.dynamicTest(" $beam => $expected") {
            val brick = 2
            val iRule = (brick shl 4) or beam
            val got = BeamCA.rules[(brick shl 4) or beam]
            assertEquals(iRule and CellularAutomata.freezedBits, got and CellularAutomata.freezedBits)
            assertEquals( expected, got and CellularAutomata.shiftedBits)
        }
    }

 @TestFactory
    fun testRuleSplitterSlash() = listOf(
        0 to 0,
        1 to 8,
        2 to 4,
        3 to 12,
        4 to 2,
        5 to 10,
        6 to 6,
        7 to 14,
        8 to 1,
        9 to 9,
        10 to 5,
        11 to 13,
        12 to 3,
        13 to 11,
        14 to 7,
        15 to 15,
    ).map { (beam: Int, expected: Int) ->
        DynamicTest.dynamicTest(" $beam => $expected") {
            val brick = 3
            val iRule = (brick shl 4) or beam
            val got = BeamCA.rules[(brick shl 4) or beam]
            assertEquals(iRule and CellularAutomata.freezedBits, got and CellularAutomata.freezedBits)
            assertEquals( expected, got and CellularAutomata.shiftedBits)
        }
    }

 @TestFactory
    fun testRuleSplitterBackslash() = listOf(
        0 to 0,
        1 to 2,
        2 to 1,
        3 to 3,
        4 to 8,
        5 to 10,
        6 to 9,
        7 to 11,
        8 to 4,
        9 to 6,
        10 to 5,
        11 to 7,
        12 to 12,
        13 to 14,
        14 to 13,
        15 to 15,
    ).map { (beam: Int, expected: Int) ->
        DynamicTest.dynamicTest(" $beam => $expected") {
            val brick = 4
            val iRule = (brick shl 4) or beam
            val got = BeamCA.rules[(brick shl 4) or beam]
            assertEquals(iRule and CellularAutomata.freezedBits, got and CellularAutomata.freezedBits)
            assertEquals( expected, got and CellularAutomata.shiftedBits)
        }
    }

}