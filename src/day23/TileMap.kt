package day23

enum class Tile() {
    Wall, Flat, GoN, GoW, GoS, GoE;

    companion object {
        fun parse(c: Char): Tile {
            return when (c) {
                '#' -> Wall
                '.' -> Flat
                //'^' -> GoN
                //'<' -> GoW
                'v' -> GoS
                '>' -> GoE
                else -> throw Exception("Unknown tile $c")
            }
        }
    }
}

data class TileMap(val tiles: List<List<Tile>>) {
    val size = tiles.size

    init {
        assert(tiles.all { tiles[0].size == it.size })
    }
    companion object {
        fun parse(input: List<String>): TileMap {
            return TileMap(input.map { l -> l.map { Tile.parse(it) } })
        }
    }
}