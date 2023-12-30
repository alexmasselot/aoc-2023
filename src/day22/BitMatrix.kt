package day22

import java.util.BitSet

/** by construction, rows[i].size() == rows.size() for all i */
data class BitMatrix(val rows: List<BitSet>) {

    val size = rows.size

    fun row(i: Int) = rows[i].clone() as BitSet
    fun col(i: Int) = BitSet(size).apply { rows.forEachIndexed { j, row -> set(j, row.get(i)) } }

    fun cols():List<BitSet>{
        return (0 until size).map { col(it) }
    }
    fun get(row: Int, col: Int): Boolean {
        return rows[row].get(col)
    }

    fun set(row: Int, col: Int, value: Boolean) {
        rows[row].set(col, value)
    }

    fun multiply(other: BitMatrix): BitMatrix {
        val newRows = (0..<size).map { i ->
            val row = BitSet()
            for (j in 0..<size) {
                row.set(j, rows[i].intersects(other.col(j)))
            }
            row
        }
        return BitMatrix(newRows)
    }


    fun nextConnected():BitMatrix{
        val newRows = rows.mapIndexed() { i, row ->
            val r = BitSet(size)
            bits(row).forEach { j ->
                r.or(row(j))
            }
            r
        }
        return BitMatrix(newRows)
    }

    fun or(other: BitMatrix):BitMatrix{
        val newRows = rows.mapIndexed() { i, row ->
            val r = row.clone() as BitSet
            r.or(other.rows[i])
            r
        }
        return BitMatrix(newRows)
    }
    fun andNot(other: BitMatrix):BitMatrix{
        val newRows = rows.mapIndexed() { i, row ->
            val r = row.clone() as BitSet
            r.andNot(other.rows[i])
            r
        }
        return BitMatrix(newRows)
    }

    fun cardinality(): Int {
        return rows.sumOf { it.cardinality() }
    }

    override fun toString() = rows.joinToString("\n") { row ->
        (0 until size).map { col ->
            if (row.get(col)) {
                'X'
            } else {
                '.'
            }
        }.joinToString("")
    }
    companion object{
        fun empty(size: Int): BitMatrix {
            val rows = (0 until size).map { BitSet(size) }
            return BitMatrix(rows)
        }

        fun bits(bs: BitSet): List<Int>{
            val xs = mutableListOf<Int>()
            var i = bs.nextSetBit(0)
            while (i >= 0) {
                xs.add(i)
                i = bs.nextSetBit(i + 1)
            }
            return xs

        }
    }
}
