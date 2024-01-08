package day24

import kotlin.math.abs

data class Position(val x: Long, val y: Long, val z: Long) {
    override fun toString() = "${x.toString().padStart(15)}, ${y.toString().padStart(15)}, ${z.toString().padStart(15)}"
}

data class Velocity(val x: Int, val y: Int, val z: Int) {
    override fun toString() = "${x.toString().padStart(5)}, ${y.toString().padStart(5)}, ${z.toString().padStart(5)}"
}

data class HailStone(val pos: Position, val vel: Velocity) {

    fun trajectoryIntersect2DInFuture(other: HailStone): Pair<Double, Double>? {
        /**
         * V = ( vx  -o.vx )
         *     ( vy  -o.vy )
         *
         * t1 is when the hailstone cross the other hailstone flying after t2
         * t = ( t1 )
         *     ( t2 )
         * delta = ( o.x-x )
         *         ( o.y-y )
         *
         * t= V^{-1} * delta
         *
         */
        val detV = -vel.x * other.vel.y + vel.y * other.vel.x

        if (detV == 0)
            return null

        val dx = other.pos.x - pos.x
        val dy = other.pos.y - pos.y

        val t1 = 1.0 / detV * (-other.vel.y * dx + other.vel.x * dy)
        val t2 = 1.0 / detV * (-vel.y * dx + vel.x * dy)

        if (t1 < 0 || t2 < 0)
            return null


        return pos.x + t1 * vel.x to pos.y + t1 * vel.y

    }

    fun trajectoryIntersect3D(other: HailStone): Pair<Triple<Double, Double, Double>, Pair<Double, Double>>? {
        val xDiff = other.pos.x -pos.x
        val yDiff = other.pos.y -pos.y
        val zDiff = other.pos.z -pos.z

        val det = -other.vel.y*vel.x+vel.y*other.vel.x
        if(det == 0)
            return null
        val t = 1.0/det * (-other.vel.y * xDiff + other.vel.x * yDiff)
        val s = 1.0/det * (-vel.y * xDiff + vel.x * yDiff)
        val intersectionX = pos.x + t * vel.x
        val intersectionY = pos.y + t * vel.y
        val intersectionZ = pos.z + t * vel.z
        val intersectionZs = other.pos.z + s * other.vel.z
        if(intersectionZ != intersectionZs){
            return null
        }

        return Triple(intersectionX, intersectionY, intersectionZ) to (t to s)
    }

    fun isColinear(other: HailStone): Boolean {
        val detV = listOf(
            -vel.x * other.vel.y + vel.y * other.vel.x,
            -vel.x * other.vel.z + vel.z * other.vel.x,
            -vel.y * other.vel.z + vel.z * other.vel.y
        ).sumOf { abs(it) }

        return detV == 0
    }

    override fun toString() = "$pos @ $vel"

    companion object {
        fun parse(input: String): HailStone {
            val re = """(\-?\d+),\s+(\-?\d+),\s+(\-?\d+) @\s+(\-?\d+),\s+(\-?\d+),\s+(\-?\d+)""".toRegex()
            val (x, y, z, vx, vy, vz) = re.matchEntire(input)!!.destructured
            return HailStone(Position(x.toLong(), y.toLong(), z.toLong()), Velocity(vx.toInt(), vy.toInt(), vz.toInt()))
        }
    }
}