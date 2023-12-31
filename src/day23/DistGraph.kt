package day23

data class Edge(val from: Int, val to: Int, val dist: Int) {
    override fun toString() = "$from -[$dist]-> $to"
}

/**
 * a graph with distance between nodes
 */
data class DistGraph(val nodes: List<Int>, val edges: List<Edge>) {

    val linkedTo: Map<Int, List<Edge>> = nodes.map { it to edges.filter { e -> e.from == it } }.toMap()
    fun addEdge(from: Int, to: Int, dist: Int) = DistGraph(nodes, edges + Edge(from, to, dist))


    fun findLongestPathDistance(from: Int, to: Int): Int {
        fun handler(pos: Int, dist: Int, visited: Set<Int>): Int {
            if (pos == to) return dist

            val next = linkedTo[pos]!!.filter { it.to !in visited }
            if (next.isEmpty())
                return -1

            return next.map { handler(it.to, dist + it.dist, visited + pos) }.max() ?: 0
        }

        return handler(from, 0, setOf())
    }

    override fun toString(): String {
        return nodes.joinToString("\n") { n ->
            val from = linkedTo[n]!!.joinToString(", ") { it.toString() }
            "$n: $from"
        }
    }

    companion object {
        fun create(nodes: List<Int>) = DistGraph(nodes, listOf())
    }
}