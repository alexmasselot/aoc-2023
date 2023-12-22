package day19

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class NodeTest {
    @Test
    fun testParseBrancherGTFollow() {
        val given = "x>787:zz"
        val got = Node.parseBrancher(given)
        assertEquals("zz", got(Part(s = 1000, x = 2000)))
    }

    @Test
    fun testParseBrancherGTNull() {
        val given = "x>787:zz"
        val got = Node.parseBrancher(given)
        assertNull(got(Part(s = 1000, x = 100)))
    }

    @Test
    fun testParseBrancherLTFollow() {
        val given = "x<787:zz"
        val got = Node.parseBrancher(given)
        assertEquals("zz", got(Part(s = 1000, x = 100)))
    }

    @Test
    fun testParseBrancherLTNull() {
        val given = "x<787:zz"
        val got = Node.parseBrancher(given)
        assertNull(got(Part(s = 1000, x = 1000)))
    }
    @Test
    fun testParseBrancherEQ() {
        val given = "zz"
        val got = Node.parseBrancher(given)
        assertEquals("zz", got(Part(s = 1000, x = 1000)))
    }

    @Test
    fun testParse(){
        val got = Node.parse("px{a<2006:qkq,m>2090:A,rfg}")
        assertEquals("px", got.name)
        assertEquals(3, got.branches.size)
    }


    @Test
    fun testNext(){
        val node = Node.parse("qqz{s>2770:qs,m<1801:hdj,R}")
        val part = Part.parse("{x=787,m=2655,a=1222,s=2876}")

        val got = node.next(part)
        assertEquals("qs", got)
    }
}