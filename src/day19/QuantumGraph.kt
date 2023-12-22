package day19

import java.util.*

data class QuantumGraph(val nodes: Map<String, QuantumNode>) {

    fun endCount(): Long {
        tailrec fun handler(acc: Long, toVisit: List<Pair<String, QuantumPart>>): Long {
            if (toVisit.isEmpty()) return acc
            val (nextNodeName, nextPart) = toVisit.first()
            val newToVisit = nodes[nextNodeName]!!.next(nextPart)
            val addAcc = newToVisit.filter { it.first == "A" }
                .sumOf { (name, part) ->
                    (0..3).fold(1L) { acc, i ->
                        val bs = part.bits.clone() as BitSet
                        bs.clear(0, i * QuantumPart.MAX)
                        bs.clear((i + 1) * QuantumPart.MAX, 4 * QuantumPart.MAX)
                        acc * bs.cardinality()
                    }
                }
            return handler(acc + addAcc, toVisit.drop(1) + newToVisit.filter { it.first != "A" && it.first != "R" })
        }

        return handler(0, listOf("in" to QuantumPart.fill()))
    }


    companion object {
        fun parse(input: List<String>): QuantumGraph {
            val nodes = input.map { QuantumNode.parse(it) }.map { it.name to it }.toMap()
            return QuantumGraph(nodes)
        }
    }
}


data class QuantumPart(val bits: BitSet) {

    override fun toString() = """{X=${bits.get(0, MAX).rangeString()}, M=${bits.get(MAX, 2 * MAX).rangeString()}, A=${bits.get(2 * MAX, 3 * MAX).rangeString()}, S=${bits.get(3 * MAX, 4 * MAX).rangeString()}}"""

    companion object {
        val MAX: Int = 4000
        fun fill(): QuantumPart {
            val bs = BitSet(MAX * 4)
            bs.set(0, MAX * 4)
            return QuantumPart(bs)
        }
    }
}

enum class CONDITION {
    LT, GT
}

enum class Category {
    X, M, A, S
}


interface QuantumBrancher {
    fun next(p: QuantumPart): Pair<String, QuantumPart>
}

fun BitSet.rangeString(): String {
    fun handler(acc: List<IntRange>, offset: Int): List<IntRange> {
        val nextBit = this.nextSetBit(offset)
        if (nextBit == -1) return acc
        val nextClear = this.nextClearBit(nextBit)
        return handler(acc.plusElement(nextBit..<nextClear), nextClear)
    }
    return handler(emptyList(), 0).joinToString(",")
}

data class QuantumBrancherCond(val category: Category, val condition: CONDITION, val value: Int, val next: String) :
    QuantumBrancher {
    private val msk = BitSet(QuantumPart.MAX * 4)
    val antimMsk = BitSet(QuantumPart.MAX * 4)

    init {
        msk.or(QuantumNode.mask[category]!!)
        antimMsk.set(0, 4 * QuantumPart.MAX)
        val sh = QuantumNode.shift[category]!!
        when (condition) {
            CONDITION.LT -> {
                msk.set(sh + 0, sh + value - 1)
                antimMsk.clear(sh + 0, sh + value - 1)

            }

            CONDITION.GT -> {
                msk.set(sh + value, sh + QuantumPart.MAX)
                antimMsk.clear(sh + value, sh + QuantumPart.MAX)
            }
        }
    }

    override fun next(p: QuantumPart): Pair<String, QuantumPart> {
        val ret = p.bits.clone() as BitSet
        ret.and(msk)
        return next to QuantumPart(ret)
    }
}

data class QuantumBrancherElse(val name: String) : QuantumBrancher {
    override fun next(p: QuantumPart): Pair<String, QuantumPart> {
        return name to p
    }
}

data class QuantumNode(val name: String, val branchers: List<QuantumBrancher>) {
    fun next(quantumPart: QuantumPart): List<Pair<String, QuantumPart>> {
        fun handler(
            acc: List<Pair<String, QuantumPart>>,
            flowingIn: QuantumPart,
            remainingBranchers: List<QuantumBrancher>
        ): List<Pair<String, QuantumPart>> {
//            println("----------------------")
//            println("flowingIn = $flowingIn")
//            println("$acc")
            if (remainingBranchers.isEmpty()) return acc

            val brancher = remainingBranchers.first()
//            println("brancher = $brancher")
            val (nextName, nextPart) = brancher.next(flowingIn)
//            println("nextName = $nextName")
//            println("nextPart = ${nextPart.bits.rangeString()}")
            val mskNext = flowingIn.bits.clone() as BitSet
            if (brancher is QuantumBrancherCond) {
                mskNext.and(brancher.antimMsk)
            }

            return handler(acc + (nextName to nextPart), QuantumPart(mskNext), remainingBranchers.drop(1))
        }

        val mskInit = BitSet(QuantumPart.MAX * 4)
        mskInit.set(0, QuantumPart.MAX * 4)
        return handler(emptyList(), quantumPart, branchers)
    }


    companion object {

        val mask = mapOf(
            Category.X to BitSet(4 * QuantumPart.MAX),
            Category.M to BitSet(4 * QuantumPart.MAX),
            Category.A to BitSet(4 * QuantumPart.MAX),
            Category.S to BitSet(4 * QuantumPart.MAX),
        )
        val shift = mapOf(
            Category.X to 0,
            Category.M to 1 * QuantumPart.MAX,
            Category.A to 2 * QuantumPart.MAX,
            Category.S to 3 * QuantumPart.MAX,
        )

        init {
            mask[Category.X]!!.set(0, QuantumPart.MAX)
            mask[Category.M]!!.set(1 * QuantumPart.MAX, 2 * QuantumPart.MAX)
            mask[Category.A]!!.set(2 * QuantumPart.MAX, 3 * QuantumPart.MAX)
            mask[Category.S]!!.set(3 * QuantumPart.MAX, 4 * QuantumPart.MAX)
            listOf(Category.X, Category.M, Category.A, Category.S).forEach { mask[it]!!.flip(0, 4 * QuantumPart.MAX) }
        }

        private val reGTLT = """([xmas])([<>])(\d+):(\w+)""".toRegex()
        private val reEQ = """(\w+)""".toRegex()
        private fun parseBrancher(input: String): QuantumBrancher {
            if (reGTLT.matches(input)) {
                val (x, cs, vs, next) = reGTLT.matchEntire(input)!!.destructured
                val v = vs.toInt()
                val cat = Category.valueOf(x.uppercase())
                val cond = if (cs == "<") CONDITION.LT else CONDITION.GT
                return QuantumBrancherCond(cat, cond, v, next)
            }
            if (reEQ.matches(input)) {
                return QuantumBrancherElse(input)
            }
            throw Exception("cannot parse function $input")

        }

        fun parse(input: String): QuantumNode {
            val re = """(\w+)\{(.*)\}""".toRegex()
            val (name, brs) = re.matchEntire(input)!!.destructured
            return QuantumNode(name, brs.split(",").map { parseBrancher(it) })
        }

    }
}
