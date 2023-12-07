package day07

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class CardTest {
    @Test
    fun fromString() {
        val given = "32T3K 42"
        val got = Card.fromString(given)

        assertEquals(Card("ONVOY", 42), got)
    }
    @TestFactory
    fun testHandType() = listOf(
        "22222" to "5",
        "AAAAT" to "41",
        "32T3K" to "2111",
        "T55J5" to "311",
        "KK677" to "221",
        "KTJJT" to "221",
        "QQQJA" to "311",
    )
        .map { (input, expected) ->
            DynamicTest.dynamicTest(" $input is handtype $expected") {
                val c = Card.fromString("$input 42")
                assertEquals(expected, c.handType())
            }
        }

    @TestFactory
    fun testCompare() = listOf(
        ("22222" to "22222") to 0,
        ("AAAAT" to "KKKK2") to 1,
        ("KKKK2" to "AAAAT") to -1,
        ("32T3K" to "24562") to 1,
        ("T55J5" to "JJJJ4") to -1,
    )
        .map { (input, expected) ->
            DynamicTest.dynamicTest(" $input compareTo: $expected") {
                val c1 = Card.fromString("${input.first} 42")
                val c2 = Card.fromString("${input.second} 42")
                assertEquals(expected, c1.compareTo(c2))
            }
        }

    @Test
    fun testSortList() {
        val given = listOf(
            Card.fromString("32T3K 42"),
            Card.fromString("T55J5 42"),
            Card.fromString("KK677 42"),
            Card.fromString("KTJJT 42"),
            Card.fromString("QQQJA 42"),
        )
        val got = Card.sortList(given)
        val expected = listOf(
            Card.fromString("32T3K 42"),
            Card.fromString("KTJJT 42"),
            Card.fromString("KK677 42"),
            Card.fromString("T55J5 42"),
            Card.fromString("QQQJA 42")
        )
        assertEquals(expected, got)
    }
}