package day07

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class Card22Test {
    @Test
    fun fromString() {
        val given = "32T3K 42"
        val got = Card2.fromString(given)

        assertEquals(Card2("POWPY", 42), got)
    }

    @Test
    fun fromStringJ() {
        val given = "J2T3K 42"
        val got = Card2.fromString(given)

        assertEquals(Card2("NOWPY", 42), got)
    }

    @TestFactory
    fun testHandType() = listOf(
        "22222" to "5",
        "AAAAT" to "41",
        "32T3K" to "2111",
        "T55J5" to "41",
        "KKJ77" to "32",
        "KTJJT" to "41",
        "QQQJA" to "41",
        "JJJJJ" to "5",
    )
        .map { (input, expected) ->
            DynamicTest.dynamicTest(" $input is handtype $expected") {
                val c = Card2.fromString("$input 42")
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
                val c1 = Card2.fromString("${input.first} 42")
                val c2 = Card2.fromString("${input.second} 42")
                assertEquals(expected, c1.compareTo(c2))
            }
        }

    @Test
    fun testSortList() {
        val given = listOf(
            Card2.fromString("32T3K 42"),
            Card2.fromString("T55J5 42"),
            Card2.fromString("KK677 42"),
            Card2.fromString("KTJJT 42"),
            Card2.fromString("QQQJA 42"),
        )
        val got = Card2.sortList(given)
        val expected = listOf(
            Card2.fromString("32T3K 42"),
            Card2.fromString("KK677 42"),
            Card2.fromString("T55J5 42"),
            Card2.fromString("QQQJA 42"),
            Card2.fromString("KTJJT 42"),
        )
        assertEquals(expected, got)
    }
}