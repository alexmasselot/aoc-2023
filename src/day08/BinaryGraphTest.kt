package day08

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BinaryGraphTest {
    val graph = BinaryGraph(
        mapOf(
            "AAA" to 0,
            "BBB" to 1,
            "CCC" to 2,
            "DDD" to 3,
            "EEE" to 4,
            "GGG" to 5,
            "ZZZ" to 6,
        ),
        listOf(1, 3, 6, 3, 4, 5, 6),
        listOf(2, 4, 5, 3, 4, 5, 6)
    )

    @Test
    fun testParseInput() {
        val given = """
            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)
        """.trimIndent().split("\n")

        val got = BinaryGraph.parseInput(given)

        assertEquals(graph, got)
    }

    @Test
    fun TestWalkUntilInt() {
        val got = graph.walkUntil(
            6,
            0,
            listOf(1, 0)
        )
        assertEquals(2, got)
    }

    @Test
    fun TestCountWalks() {
        val got = graph.countWalks(
            "ZZZ",
            "AAA",
            "RL"
        )
        assertEquals(2, got)
    }

}