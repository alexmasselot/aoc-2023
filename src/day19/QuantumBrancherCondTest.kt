package day19

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.BitSet

class QuantumBrancherCondTest {
    @Test
    fun testNextLT() {
        val brancher = QuantumBrancherCond(Category.X, CONDITION.LT, 5, "zz")
        val bs = BitSet(QuantumPart.MAX * 4)
        bs.set(0, 10)
        bs.set(QuantumPart.MAX, 4 * QuantumPart.MAX)
        val givenPart = QuantumPart(bs)

        val (gotName, gotPart) = brancher.next(givenPart)

        assertEquals(bs.cardinality(), 12010)
        assertEquals("zz", gotName)
        val expectedBS = BitSet(QuantumPart.MAX * 4)
        expectedBS.set(0, 4)
        expectedBS.set(QuantumPart.MAX, 4 * QuantumPart.MAX)
        assertEquals(QuantumPart(expectedBS), gotPart)
    }

    @Test
    fun testNextGT() {
        val brancher = QuantumBrancherCond(Category.X, CONDITION.GT, 5, "zz")
        val bs = BitSet(QuantumPart.MAX * 4)
        bs.set(0, 10)
        bs.set(QuantumPart.MAX, 4 * QuantumPart.MAX)
        val givenPart = QuantumPart(bs)

        val (gotName, gotPart) = brancher.next(givenPart)

        assertEquals("zz", gotName)
        val expectedBS = BitSet(QuantumPart.MAX * 4)
        expectedBS.set(5, 10)
        expectedBS.set(QuantumPart.MAX, 4 * QuantumPart.MAX)
        assertEquals(QuantumPart(expectedBS), gotPart)
    }
}