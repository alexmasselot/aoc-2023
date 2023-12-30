package day22

import java.util.BitSet

data class Point(val x: Int, val y: Int, val z: Int) {
    fun dropyBy(n: Int): Point {
        return Point(x, y, z - n)
    }

    override fun toString() = "$x,$y,$z"
}

data class Brick(val p1: Point, val p2: Point) {
    fun dropBy(n: Int): Brick {
        return Brick(p1.dropyBy(n), p2.dropyBy(n))
    }

    fun verticalDistanceTo(other: Brick): Int {
        return p1.z - other.p2.z - 1
    }

    fun isBellow(other: Brick): Boolean {
        return ((!(p1.x > other.p2.x || p2.x < other.p1.x)) && !(p1.y > other.p2.y || p2.y < other.p1.y)) && p2.z < other.p1.z
    }

    override fun toString() = "$p1~$p2"

    companion object {
        fun parse(input: String): Brick {
            val re = "(\\d+),(\\d+),(\\d+)~(\\d+),(\\d+),(\\d+)".toRegex()
            val vs = re.matchEntire(input)!!.groupValues.drop(1).map { it.toInt() }
            return Brick(Point(vs[0], vs[1], vs[2]), Point(vs[3], vs[4], vs[5]))
        }
    }
}

data class BrickMap(val bricks: List<Brick>) {
    val size = bricks.size

    fun dropBY(iBrick: Int, z: Int) = BrickMap(
        bricks.take(iBrick)
            .plusElement(bricks.get(iBrick).dropBy(z))
            .plus(bricks.drop(iBrick + 1))
    )

    /**
     * [i][j] <=> [i] is bellow [j], even if there are intermediaries between them
     */
    fun isBellowMatrix(): BitMatrix {
        val size = bricks.size
        val matrix = BitMatrix.empty(size)
        for (i in 0 until size) {
            for (j in 0 until size) {
                if (bricks[i].isBellow(bricks[j])) {
                    matrix.set(i, j, true)
                }
            }
        }
        return matrix
    }

    /**
     * [i][j] <=> [i] is catching [j], and there is nothing in between them
     */
    fun isCatchingMatrix(): BitMatrix {
        val ibm = isBellowMatrix()
        fun handler(cleaned: BitMatrix, indirect: BitMatrix): BitMatrix {
            val next = cleaned.or(indirect).nextConnected()
            val nextCleaned = cleaned.andNot(next)
            if (nextCleaned == cleaned) {
                return cleaned
            }
            return handler(nextCleaned, next)
        }
        return handler(ibm, ibm)
    }

    /**
     * [i][j] means that j is sitting on the top of i
     */
    fun isSupportingMatrix(): BitMatrix {
        val cm = isCatchingMatrix()
        (0..<size).forEach { row ->
            (0..<size).forEach { col ->
                if (cm.get(row, col) && bricks[col].p1.z > bricks[row].p2.z + 1) {
                    cm.set(row, col, false)
                }

            }
        }
        return cm
    }

    fun indexesMonoSupports(): List<Int> {
        val dropper = BricksDropper(isCatchingMatrix())
        val sm = dropper.dropThemAll(this).isSupportingMatrix()
        val bAcc = BitSet(sm.size)
        sm.cols.filter { it.cardinality() == 1 }.forEach { bAcc.or(it) }
        return BitMatrix.bits(bAcc)
    }

    override fun toString() = bricks.joinToString("\n") { it.toString() }

    companion object {
        fun parse(input: List<String>): BrickMap {
            return BrickMap(input.map { Brick.parse(it) })
        }
    }
}

class BricksDropper(private val catchingMatrix: BitMatrix) {
    val size = catchingMatrix.size

    fun dropToGround(brickMap: BrickMap): BrickMap {
        val xs = catchingMatrix.cols.mapIndexedNotNull() { i, col ->
            if (col.cardinality() == 0) {
                i
            } else {
                null
            }
        }.toSet()

        val bricks = brickMap.bricks.mapIndexed { i, brick ->
            if (xs.contains(i)) {
                brick.dropBy(brick.p1.z - 1)
            } else {
                brick
            }
        }
        return BrickMap(bricks)
    }

    fun dropThemAll(brickMap: BrickMap): BrickMap {
        val grounded = dropToGround(brickMap)

        tailrec fun handler(accMap: BrickMap, accFAllen: Set<Int>): BrickMap {
            if (accFAllen.size == brickMap.size) {
                return accMap
            }
            val (iNext, bNext) = accMap.bricks.mapIndexed { i, b -> i to b }
                .filter { (i, _) -> !accFAllen.contains(i) }
                .minBy { it.second.p1.z }
            val bCatching = BitMatrix.bits(catchingMatrix.cols[iNext])
                .map { accMap.bricks[it] }
                .maxBy { it.p2.z }
            val zDrop = bNext.p1.z - bCatching.p2.z - 1
            return handler(accMap.dropBY(iNext, zDrop), accFAllen.plus(iNext))

        }
        return handler(
            grounded,
            grounded.bricks.mapIndexed { i, b -> i to b }.filter { it.second.p1.z == 1 }.map { it.first }.toSet()
        )
    }
}
