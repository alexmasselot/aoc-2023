package day12

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class BlockFinderTest {
    @TestFactory
    fun testFindBlock() = listOf(
        ("..###.." to 3) to 2,
        ("..###.." to 2) to null,
        ("..###.." to 4) to null,
        ("....###" to 3) to 4,
        ("###..." to 3) to 0,
        ("..###..###.." to 3) to 2,
        ("..###.###.." to 3) to 2,
        ("..###..##." to 2) to null,
        ("..??#.." to 3) to 2,
        ("..??#.." to 2) to 3,
        ("..?#?.." to 2) to 2,
        ("..??????.." to 2) to 2,
        ("..??.??." to 2) to 2,
        ("..??#??." to 2) to 3,
        ("..??#???." to 2) to 3,
    ).map { (input: Pair<String, Int>, expected: Int?) ->
        DynamicTest.dynamicTest(" $input => $expected") {
            val str = input.first
            val len = input.second
            val got = BlockFinder.findBlock(str, len)
            assertEquals(expected, got)
        }
    }

    @Test
    fun testFindBlockOffset() {
        val got = BlockFinder.findBlock("..?#?..", 2, 3)
        assertEquals(3, got)
    }

    @TestFactory
    fun testFindAllBlocks() = listOf(
        ("..###.." to 3) to listOf(2),
        ("..###.." to 2) to emptyList(),
        ("..###.." to 4) to emptyList(),
        ("....###" to 3) to listOf(4),
        ("###..." to 3) to listOf(0),
        ("..###..###.." to 3) to listOf(2),
        ("..###.###.." to 3) to listOf(2),
        ("..###..##." to 2) to emptyList(),
        ("#..###.." to 3) to emptyList(),
        (".#..###.." to 3) to emptyList(),
        ("..??#.." to 3) to listOf(2),
        ("..??#.." to 2) to listOf(3),
        ("..?#?.." to 2) to listOf(2, 3),
        ("..??????.." to 2) to listOf(2, 3, 4, 5, 6),
        ("..??.??." to 2) to listOf(2, 5),
        ("..??#??." to 2) to listOf(3, 4),
        ("..??#???." to 2) to listOf(3, 4),
        ("???" to 1) to listOf(0, 1, 2),
        ("???##???##?#??#?#..#" to 1) to listOf(0, 1),
    ).map { (input: Pair<String, Int>, expected: List<Int>) ->
        DynamicTest.dynamicTest(" $input => $expected") {
            val str = input.first
            val len = input.second

            val got = BlockFinder.findAllBlocks(str, len)
            assertEquals(expected, got.toList())
        }
    }

    @TestFactory
    fun testCountBlocks() = listOf(
        (".#.#?." to listOf(1, 1)) to 1,
        ("..###.." to listOf(3)) to 1,
        ("..###.." to listOf(2)) to 0,
        ("..###.." to listOf(4)) to 0,
        ("..??#.." to listOf(3)) to 1,
        ("..??#.." to listOf(2)) to 1,
        ("..?#?.." to listOf(2)) to 2,
        ("..??????.." to listOf(2)) to 5,
        ("..??.??." to listOf(2)) to 2,
        ("..??#??." to listOf(2)) to 2,
        ("..??#???." to listOf(2)) to 2,
        (".##.?#??.#.?#" to listOf(2, 1, 1, 1)) to 1,
        /*
        * .######....#.
        * ..######...#.
        * ...######..#.
        * ....######.#.
         */
        (".???#??????#." to listOf(6, 1) to 4),
        /*
         * ???##???##?#??#?#..# [1, 14, 1]
         * #..##############..#
         * .#.##############..#
         */
        ("???##???##?#??#?#..#" to listOf(1, 14, 1) to 2),
        /*
        * ?##?.??.???.. [3, 2, 2] => 4
        * ###..##.##...
        * ###..##..##..
        * .###.##.##...
        * .###.##..##..
         */
        ("?##?.??.???.." to listOf(3, 2, 2) to 4),
        /*
         * ???.?????##?#??????? [3,8,1 ]"
         * ###..########.#.....
         * ###..########..#....
         * ###..########...#...
         * ###..########....#..
         * ###..########.....#.
         * ###..########......#
         * ###...########.#....
         * ###...########..#...
         * ###...########...#..
         * ###...########....#.
         * ###...########.....#
         * ###....########.#...
         * ###....########..#..
         * ###....########...#.
         * ###....########....#
         * ###.....########.#..
         * ###.....########..#.
         * ###.....########...#
         * ###......########.#.
         * ###......########.#.
         * ###.......########.#
         * ....###.########.#..
         * ....###.########..#.
         * ....###.########...#
         * ....###..########.#.
         * ....###..########..#
         * .....###.########.#.
         * .....###.########..#
         */
        ("???.?????##?#???????" to listOf(3, 8, 1) to 27),
        (".??..??...?##.?.??..??...?##.?.??..??...?##.?.??..??...?##.?.??..??...?##." to listOf(
            1,
            1,
            3,
            1,
            1,
            3,
            1,
            1,
            3,
            1,
            1,
            3,
            1,
            1,
            3
        )) to 16384
    ).map { (input: Pair<String, List<Int>>, expected: Int) ->
        DynamicTest.dynamicTest(" $input => $expected") {
            val str = input.first
            val lens = input.second

            val got = BlockFinder.countBlocks(str, lens)
            assertEquals(expected, got)
        }
    }

    @Test
    fun testCountBlocks1() {
        val input = ".##.?#??.#.?#" to listOf(2, 1, 1, 1)
        val expected = 1

        val got = BlockFinder.countBlocks(input.first, input.second)
        assertEquals(expected, got)

    }

    @TestFactory
    fun testParseCount() = listOf(
        ("???.### 1,1,3") to 1,
        (".??..??...?##. 1,1,3") to 4,
        ("?#?#?#?#?#?#?#? 1,3,1,6") to 1,
        ("????.#...#... 4,1,1") to 1,
        ("????.######..#####. 1,6,5") to 4,
        ("?###???????? 3,2,1") to 10,
    ).map { (input: String, expected: Int) ->
        DynamicTest.dynamicTest(" $input => $expected") {
            val (str, lens) = BlockFinder.parseLine(input, 1)

            val got = BlockFinder.countBlocks(str, lens)
            assertEquals(expected, got)
        }
    }
}