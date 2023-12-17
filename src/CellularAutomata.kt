/**
 * apply a rule to each cell and propagates:
 * - bit 0 to north
 * - bit 1 to west
 * - bit 2 to south
 * - bit 3 to east
 *  other bits remains in positions
 *  By default, the propagation is not cyclic
 */
data class CellularAutomata(val grid: List<List<Int>>, val rules: List<Int>) {
    fun next(): CellularAutomata {
        val ruled = CellularAutomata(
            grid.mapMatrix { rules[it] },
            rules
        )
        val toPropagate = ruled.grid.mapMatrix { it and shiftedBits }
        val propagated = toPropagate.mapMatrix { it and maskN }.shiftN(0)
            .bitOr(toPropagate.mapMatrix { it and maskW }.shiftW(0))
            .bitOr(toPropagate.mapMatrix { it and maskS }.shiftS(0))
            .bitOr(toPropagate.mapMatrix { it and maskE }.shiftE(0))

        return CellularAutomata(
            grid.mapMatrix { it and freezedBits }
                .bitOr(propagated),
            rules
        )
    }

    companion object {
        val freezedBits = "111111111111111111111111110000".toInt(2)
        val shiftedBits = "1111".toInt(2)
        val maskN = "1".toInt(2)
        val maskW = "10".toInt(2)
        val maskS = "100".toInt(2)
        val maskE = "1000".toInt(2)
    }
}