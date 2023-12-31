package day23

import kotlin.math.sqrt

enum class Move(val mask: Int) {
    N(0b1), W(0b10), S(0b100), E(0b1000)
}

data class GraphMap(val tiles: List<Int>) {
    val size = sqrt(tiles.size.toDouble()).toInt()
    val startPos = 1
    val endPos = tiles.size - 2


    fun findLongest(): Int {
        tailrec fun handler(pos: Int, dist: Int, visited: Set<Int>, distAcc: List<Int>): List<Int> {
            if (pos == endPos) return distAcc.plusElement(dist)

            val next = next(pos).filter { it !in visited }
            if (next.isEmpty())
                return distAcc

            return next.flatMap { handler(it, dist + 1, visited + pos, distAcc) }
        }
        val distances = handler(startPos, 0, setOf(), listOf())
        println(distances)
        return distances.max()
    }

    fun next(i: Int): List<Int> {
        val x = tiles[i]
        return listOfNotNull(
            if (x and Move.N.mask != 0) i - size else null,
            if (x and Move.W.mask != 0) i - 1 else null,
            if (x and Move.S.mask != 0) i + size else null,
            if (x and Move.E.mask != 0) i + 1 else null
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