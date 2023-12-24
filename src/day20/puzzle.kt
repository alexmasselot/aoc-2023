package day20

import findLCM
import println
import readInput

enum class PulseIntensity {
    HIGH, LOW
}

data class Pulse(val from: String, val to: String, val intensity: PulseIntensity) {
    override fun toString() = "$from -${intensity.toString().lowercase()}-> $to"
}

abstract class Module {
    abstract val name: String
    abstract val symbol: String

    val connectedTo = mutableListOf<String>()
    val connectedFrom = mutableListOf<String>()

    private val nbPulses = mutableMapOf(PulseIntensity.HIGH to 0L, PulseIntensity.LOW to 0L)
    abstract fun receiveAndEmit(pulse: Pulse): List<Pulse>

    abstract fun connectFrom(from: String)

    fun connectedTo(to: String) {
        connectedTo.add(to)
    }

    fun pushPulse(pulse: Pulse) {
        nbPulses[pulse.intensity] = nbPulses[pulse.intensity]!! + 1
    }

    fun countPushedPulse(pi: PulseIntensity) = nbPulses[pi]!!

    fun resetPulseCount() {
        nbPulses[PulseIntensity.HIGH] = 0
        nbPulses[PulseIntensity.LOW] = 0
    }

    override fun toString() = "$symbol$name (<-${connectedFrom.size} ->${connectedTo.size})"

}

class Broadcaster() : Module() {
    override val name: String = "broadcaster"
    override val symbol = ">"

    override fun receiveAndEmit(pulse: Pulse) =
        connectedTo.map { Pulse(name, it, pulse.intensity) }

    override fun connectFrom(from: String) {
    }

}

class Output(override val name: String) : Module() {
    override val symbol = "/"

    override fun receiveAndEmit(pulse: Pulse): List<Pulse> {
        pushPulse(pulse)
        return emptyList()
    }

    override fun connectFrom(from: String) {
        connectedFrom.add(from)
    }
}

class FlipFlop(override val name: String) : Module() {
    override val symbol = "%"
    var isOff = true

    override fun receiveAndEmit(pulse: Pulse): List<Pulse> {
        pushPulse(pulse)
        if (pulse.intensity == PulseIntensity.HIGH) {
            return emptyList()
        }

        isOff = !isOff
        val pi = if (isOff) PulseIntensity.LOW else PulseIntensity.HIGH
        return connectedTo.map { Pulse(name, it, pi) }
    }

    override fun connectFrom(from: String) {
        connectedFrom.add(from)
    }

}

class Conjunction(override val name: String) : Module() {
    override val symbol = "&"
    val connectedFromPulses = mutableMapOf<String, PulseIntensity>()

    fun allHigh() = connectedFromPulses.values.all { it == PulseIntensity.HIGH }
    override fun receiveAndEmit(pulse: Pulse): List<Pulse> {
        pushPulse(pulse)
        connectedFromPulses[pulse.from] = pulse.intensity
        val pi = if (allHigh()) {
            PulseIntensity.LOW
        } else {
            PulseIntensity.HIGH
        }

        return connectedTo.map { Pulse(name, it, pi) }
    }

    override fun connectFrom(from: String) {
        connectedFrom.add(from)
        connectedFromPulses[from] = PulseIntensity.LOW
    }

}

class ModuleConfig {
    val modules = mutableMapOf<String, Module>()

    val fifo = mutableListOf<Pulse>()

    fun pressButton() {
        fifo.add(Pulse("button", "broadcaster", PulseIntensity.LOW))
    }

    fun step(): Pulse? {
        if (fifo.isEmpty()) return null
        val pulse = fifo.removeFirst()
        modules[pulse.to]!!.receiveAndEmit(pulse).forEach { fifo.add(it) }
        return pulse
    }

    fun pressAndCollect(): List<Pulse> {
        fun handler(acc: List<Pulse>): List<Pulse> {
            val pulse = step() ?: return acc
            return handler(acc + pulse)
        }

        pressButton()
        return handler(emptyList())
    }

    fun pressAndSum(): Pair<Long, Long> {
        fun handler(acc: Pair<Long, Long>): Pair<Long, Long> {
            val pulse = step() ?: return acc
            if (pulse.intensity == PulseIntensity.LOW) {
                return handler((acc.first + 1) to acc.second)
            } else {
                return handler(acc.first to (acc.second + 1))
            }
        }

        pressButton()
        return handler(0L to 0L)
    }

    fun pressAndCountHighIntervals(watched: Conjunction):Map<String, List<IntRange>> {
        val clockHigh: MutableMap<String, List<IntRange>> =
            watched.connectedFrom.associateWith { emptyList<IntRange>() }.toMutableMap()
        val isHigh: MutableMap<String, Int?> = watched.connectedFrom.associateWith { null }.toMutableMap()

        fun handler(t: Int) {
            val pulse = step() ?: return
            if (pulse.to == watched.name) {
                if (pulse.intensity == PulseIntensity.LOW && isHigh[pulse.from]?.let{it != null} == true) {
                    clockHigh[pulse.from] = clockHigh[pulse.from]!!.plusElement(isHigh[pulse.from]!!..<t)
                }
                if(pulse.intensity == PulseIntensity.HIGH) {
                    isHigh[pulse.from] = t
                } else {
                    isHigh[pulse.from] = null
                }
            }
            return handler(t + 1)
        }

        pressButton()
         handler(0)
        return clockHigh.toMap()
    }

    fun resetPulseCount() {
        modules.values.forEach { it.resetPulseCount() }
    }

    fun addModule(module: Module) {
        modules[module.name] = module
    }

    fun connect(from: String, to: String) {
        if (to == "") return
        if (!modules.containsKey(to)) {
            println("adding output $to")
            addModule(Output(to))
        }
        modules[from]!!.connectedTo(to)
        modules[to]!!.connectFrom(from)
    }

    fun precursors(node: String, depth: Int): List<Module> {
        fun handler(nodes: List<String>, d: Int): List<String> {
            if (d == 0) return nodes
            return handler(nodes.flatMap { modules[it]!!.connectedFrom }, d - 1)
        }

        return handler(listOf(node), depth).map { modules[it]!! }
    }

    fun printToGraph(node: String) {
        fun handler(nodes: List<String>, visited: Set<String>) {
            if (nodes.isEmpty()) return
            val n = nodes.first()
            if (visited.contains(n))
                return handler(nodes.drop(1), visited)
            println("${modules[n]} <- ${modules[n]!!.connectedFrom.map { modules[it] }} -> ${modules[n]!!.connectedTo.map { modules[it] }}")
            handler(nodes.drop(1).plus(modules[n]!!.connectedFrom), visited + n)
        }
        handler(listOf(node), emptySet())
    }

    fun parse(input: List<String>) {
        modules.clear()
        input.forEach { line ->
            val (from, _) = line.split(" -> ")
            val mod = when {
                from == "broadcaster" -> Broadcaster()
                from.startsWith("%") -> FlipFlop(from.drop(1))
                from.startsWith("&") -> Conjunction(from.drop(1))
                else -> throw Exception("Unknown module $from")
            }
            addModule(mod)
        }
        input.forEach { line ->
            val (from, to) = line.split(" -> ")
            to.split(",").forEach { connect(from.replace("[%&]".toRegex(), ""), it.trim()) }
        }
    }
}

fun main() {
    val day = ::main.javaClass.`package`.name.takeLast(2)

    fun part1(input: List<String>): Long {
        val mc = ModuleConfig()
        mc.parse(input)

        val (nbLow, nbHigh) = (1..1000).fold(0L to 0L) { acc, _ ->
            val (low, high) = mc.pressAndSum()
            acc.first + low to acc.second + high
        }
        return nbLow * nbHigh
    }

    fun part2(input: List<String>): Long {
        val mc = ModuleConfig()
        mc.parse(input)
        println("--------------------------")
        println(mc.precursors("rx", 0))
        println(mc.precursors("rx", 1))
        println(mc.precursors("rx", 2))
        println(mc.precursors("rx", 3))

        val anteExit = mc.precursors("rx", 1).first().let { it as Conjunction }
        println(anteExit)

        val hlFirstLoop = mutableMapOf<String,Int>()
        (1..50000).forEach { i ->
            mc.resetPulseCount()
            val switchHigh = mc.pressAndCountHighIntervals( anteExit)
            if(switchHigh.values.any{it.isNotEmpty()}) {
                switchHigh.filter { it.value.isNotEmpty() }.forEach { (k, v) ->
                    if(hlFirstLoop[k] == null) {
                        hlFirstLoop[k] = i
                    }
                }
                println("$i\t"+anteExit.connectedFrom.map{n->switchHigh[n]!!.joinToString(",")+ " ".repeat(10-switchHigh[n]!!.joinToString(",").length)}.joinToString(" "))
                check(switchHigh.values.all{it.size <= 1})
                check(switchHigh.values.filter{it.size == 1}.all{it.first().contains(55)})
            }
        }
        return findLCM(hlFirstLoop.values.map{it.toLong()})

    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day$day/input_test")
    val p1 = part1(testInput)
    println(p1)
    check(part1(testInput) == 32000000L)

//    val p2 = part2(testInput)
//    println(p2)
//    check( p2 == 281)

    val input = readInput("day$day/input")
    part1(input).println()
    part2(input).println()
}
