package day23.explo

import day23.GraphMap
import day23.TileMap
import readInput

fun main() {
    val day = "23"


    fun explore(input: List<String>): Int {
        val tileMap = TileMap.parse(input.map { it.replace("""[<>v^]""".toRegex(), ".") })
        //val tileMap = TileMap.parse(input)
        val graphMap = GraphMap.build(tileMap)
        val slopeNodes = graphMap.tiles.mapIndexed { i, j -> i to j }.filter { it.second.countOneBits() == 1 }
        val nodes = graphMap.findBifurcations()

        println("bifurcations: ${nodes.size} slope nodes: ${slopeNodes.size}")
        val dGraph = graphMap.bifurcationsGraph()
        print(dGraph.findLongestPathDistance(graphMap.startPos, graphMap.endPos))
        return 42
    }

    // test if implementation meets criteria from the description, like:

    val input = readInput("day$day/input")
    explore(input)
}
