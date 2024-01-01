package day24

data class Position(val x: Long, val y: Long, val z: Long) {
}

data class Velocity(val x: Int, val y: Int, val z: Int) {
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

        val t1 = 1.0/detV *(-other.vel.y*dx+other.vel.x*dy)
        val t2 = 1.0/detV *(-vel.y*dx+vel.x*dy)

        if(t1<0 || t2<0)
            return null

        if(t1 == t2){
            println("alleluïa")
        }


        return pos.x+t1*vel.x to pos.y+t1*vel.y



    }

    companion object {
        fun parse(input: String): HailStone {
            val re = """(\-?\d+),\s+(\-?\d+),\s+(\-?\d+) @\s+(\-?\d+),\s+(\-?\d+),\s+(\-?\d+)""".toRegex()
            val (x, y, z, vx, vy, vz) = re.matchEntire(input)!!.destructured
            return HailStone(Position(x.toLong(), y.toLong(), z.toLong()), Velocity(vx.toInt(), vy.toInt(), vz.toInt()))
        }
    }
}