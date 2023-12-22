package day19

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PartTest{
    @Test
    fun testParse(){
        val given = "{x=787,m=2655,a=1222,s=2876}"
        val got = Part.parse(given)
        assertEquals(Part(787,2655,1222,2876), got)
    }
}