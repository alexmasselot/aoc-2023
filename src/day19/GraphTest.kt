package day19

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GraphTest {
    val input = """
        px{a<2006:qkq,m>2090:A,rfg}
        pv{a>1716:R,A}
        lnx{m>1548:A,A}
        rfg{s<537:gd,x>2440:R,A}
        qs{s>3448:A,lnx}
        qkq{x<1416:A,crn}
        crn{x>2662:A,R}
        in{s<1351:px,qqz}
        qqz{s>2770:qs,m<1801:hdj,R}
        gd{a>3333:R,R}
        hdj{m>838:A,pv}
    """.trimIndent().lines()

    @Test
    fun testNext() {
        val graph = Graph.parse(input)
        val part = Part.parse("{x=787,m=2655,a=1222,s=2876}")
        val got = graph.end(part)
        assertEquals("A", got)
    }

}