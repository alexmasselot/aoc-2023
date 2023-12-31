package day23

import kotlin.math.sqrt

enum class Move(val mask: Int) {
    N(0b1), W(0b10), S(0b100), E(0b1000)
}

data class GraphMap(val tiles: List<Int>) {
    val size = sqrt(tiles.size.toDouble()).toInt()
    val startPos = 1
    val endPos = tiles.size - 2


    fun findLongestBrutal(): Int {
        tailrec fun handler(comingFrom: Move, pos: Int, dist: Int, visited: Set<Int>, distAcc: List<Int>): List<Int> {
            if (pos == endPos) return distAcc.plusElement(dist)

            val next = next(comingFrom, pos).filter { it.second !in visited }
            if (next.isEmpty())
                return distAcc

            return next.flatMap { handler(it.first, it.second, dist + 1, visited + pos, distAcc) }
        }

        val distances = handler(Move.S, startPos, 0, setOf(), listOf())
        return distances.max()
    }

    fun findBifurcations(): List<Int> {
        return tiles.mapIndexed { i, j -> i to j }.filter { it.second.countOneBits() > 2 }.map { it.first }
    }

    fun bifurcationsGraph(): DistGraph {
        val nodeGraph = DistGraph.create(listOf(startPos).plus(findBifurcations()).plus(endPos))

        fun pathToNextBifurcations(iNode: Int): List<Pair<Int, Int>> {
            val startDirections = next(null, iNode)

            tailrec fun nextBifurcation(comingFrom: Move, pos: Int, dAcc: Int): Pair<Int, Int>? {
                if (tiles[pos].countOneBits() > 2 || pos == startPos || pos == endPos) {
                    return pos to dAcc
                }
                val nexts = next(comingFrom, pos)
                if(nexts.isEmpty())
                    return null
                assert(nexts.size == 1)
                return nextBifurcation(nexts[0].first, nexts[0].second, dAcc + 1)
            }
            return startDirections.map { (move, pos) ->
                nextBifurcation(move, pos, 1)
            }.filterNotNull()
        }

       return nodeGraph.nodes.fold(nodeGraph) { acc, node ->
            val edges = pathToNextBifurcations(node)
            edges.fold(acc) { acc2, (to, dist) ->
                acc2.addEdge(node, to, dist)
            }
        }

    }

    fun next(comingFrom: Move?, i: Int): List<Pair<Move, Int>> {
        val x = tiles[i]
        return listOfNotNull(
            if (x and Move.N.mask != 0 && comingFrom != Move.N) Move.S to (i - size) else null,
            if (x and Move.W.mask != 0 && comingFrom != Move.W) Move.E to (i - 1) else null,
            if (x and Move.S.mask != 0 && comingFrom != Move.S) Move.N to (i + size) else null,
            if (x and Move.E.mask != 0 && comingFrom != Move.E) Move.W to (i + 1) else null
        )
    }

    companion object {
        fun build(tileMap: TileMap): GraphMap {
            val n = tileMap.size
            val vs = tileMap.tiles.flatMapIndexed { r, row ->
                row.mapIndexed { c, tile ->
                    when (tile) {
                        Tile.Wall -> 0
                        Tile.GoN -> Move.N.mask
                        Tile.GoW -> Move.W.mask
                        Tile.GoS -> Move.S.mask
                        Tile.GoE -> Move.E.mask
                        Tile.Flat -> {
                            (if (r > 0 && tileMap.tiles[r - 1][c] != Tile.Wall) Move.N.mask else 0) or
                                    (if (c > 0 && tileMap.tiles[r][c - 1] != Tile.Wall) Move.W.mask else 0) or
                                    (if (r < n - 1 && tileMap.tiles[r + 1][c] != Tile.Wall) Move.S.mask else 0) or
                                    (if (c < n - 1 && tileMap.tiles[r][c + 1] != Tile.Wall) Move.E.mask else 0)
                        }
                    }
                }

            }
            return GraphMap(vs)

        }
    }
}