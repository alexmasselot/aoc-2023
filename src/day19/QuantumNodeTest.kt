package day19

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class QuantumNodeTest {
    @Test
    fun testNext() {
        val node = QuantumNode.parse("qqz{s>2770:qs,m<1801:hdj,R}")
        val part = QuantumPart.fill()


        val got = node.next(part)
        assertEquals(3, got.size)

        val nodeMsk = got.toMap()
        // {x=787,m=2655,a=1222,s=2876} -> qs
        val nodeQS = nodeMsk["qs"]!!
        assertTrue(nodeQS.bits.get(786))
        assertTrue(nodeQS.bits.get(4000+2654))
        assertTrue(nodeQS.bits.get(8000+1222))
        assertFalse(nodeQS.bits.get(12000+2768))
        assertTrue(nodeQS.bits.get(12000+2875))

        //{x=787,m=100,a=1222,s=100} -> hdj
        val nodeHDJ = nodeMsk["hdj"]!!
        assertTrue(nodeHDJ.bits.get(786))
        assertFalse(nodeHDJ.bits.get(4000+2654))
        assertTrue(nodeHDJ.bits.get(4000+1799))
        assertTrue(nodeHDJ.bits.get(8000+1222))
        assertTrue(nodeHDJ.bits.get(12000+0))
        assertTrue(nodeHDJ.bits.get(12000+1000))
        assertFalse(nodeHDJ.bits.get(12000+3000))

        //{x=787,m=2000,a=1222,s=100} -> R
        val nodeR = nodeMsk["R"]!!
        assertTrue(nodeR.bits.get(786))
        assertTrue(nodeR.bits.get(4000+2654))
        assertFalse(nodeR.bits.get(4000+1799))
        assertTrue(nodeR.bits.get(8000+1222))
        assertTrue(nodeR.bits.get(12000+0))
        assertTrue(nodeR.bits.get(12000+1000))
        assertFalse(nodeR.bits.get(12000+3000))
    }
}