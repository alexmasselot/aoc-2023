package day23

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GraphMapTest {
    val input = """
        #.#####################
        #.......#########...###
        #######.#########.#.###
        ###.....#.>.>.###.#.###
        ###v#####.#v#.###.#.###
        ###.>...#.#.#.....#...#
        ###v###.#.#.#########.#
        ###...#.#.#.......#...#
        #####.#.#.#######.#.###
        #.....#.#.#.......#...#
        #.#####.#.#.#########v#
        #.#...#...#...###...>.#
        #.#.#v#######v###.###v#
        #...#.>.#...>.>.#.###.#
        #####v#.#.###v#.#.###.#
        #.....#...#...#.#.#...#
        #.#########.###.#.#.###
        #...###...#...#...#.###
        ###.###.#.###v#####v###
        #...#...#.#.>.>.#.>.###
        #.###.###.#.###.#.#v###
        #.....###...###...#...#
        #####################.#
    """.trimIndent().lines()

    val inputNoSlippery = input.map{it.replace("""[<>v^]""".toRegex(), ".")}
    private val tileMap = TileMap.parse(input)
    private val tileMapNoSlippery = TileMap.parse(inputNoSlippery)

    @Test
    fun build() {
        val graphMap = GraphMap.build(tileMap)
        assertEquals(23, graphMap.size)
        assertEquals(0b000, graphMap.tiles[0])
        assertEquals(0b100, graphMap.tiles[1])
        assertEquals(0b1001, graphMap.tiles[23 * 1 + 1])
        assertEquals(0b1000, graphMap.tiles[23 * 3 + 10])
        assertEquals(0b1110, graphMap.tiles[23 * 3 + 11])
    }

    @Test
    fun findLongest(){
        val graphMap = GraphMap.build(tileMap)
        assertEquals(94, graphMap.findLongestBrutal())
    }

    @Test
    fun `find Longuest removing slippery`(){
        val graphMap = GraphMap.build(tileMapNoSlippery)
        assertEquals(154, graphMap.findLongestBrutal())
    }
}