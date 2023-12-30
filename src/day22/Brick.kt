package day22

import java.util.BitSet

data class Point(val x: Int, val y: Int, val z: Int)
data class Brick(val p1: Point, val p2: Point) {

    fun isBellow(other: Brick): Boolean {
        return ((!(p1.x > other.p2.x || p2.x < other.p1.x)) && !(p1.y > other.p2.y || p2.y < other.p1.y)) && p2.z < other.p1.z
    }

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
    fun isBellowtMatrix(): BitMatrix {
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

    fun supportMatrix(): BitMatrix {
        val ibm = isBellowtMatrix()
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

    fun countMonoSupports(): Int {
        val sm = supportMatrix()
        val bAcc = BitSet(sm.size)
        sm.cols().filter { it.cardinality() == 1 }.forEach { bAcc.or(it) }
        return bAcc.cardinality()
    }

    companion object {
        fun parse(input: List<String>): BrickMap {
            return BrickMap(input.map { Brick.parse(it) })
        }
    }
}
