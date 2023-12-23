package day20

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

    val connectedTo = mutableListOf<String>()
    abstract fun receiveAndEmit(pulse: Pulse): List<Pulse>

    abstract fun connectedFrom(from: String)

    fun connectedTo(to: String) {
        connectedTo.add(to)
    }

    companion object {

    }
}

class Broadcaster() : Module() {
    override val name: String = "broadcaster"

    override fun receiveAndEmit(pulse: Pulse) =
        connectedTo.map { Pulse(name, it, pulse.intensity) }

    override fun connectedFrom(from: String) {
    }
}

class Output(override val name: String) : Module() {

    override fun receiveAndEmit(pulse: Pulse): List<Pulse> {
        return emptyList()
    }

    override fun connectedFrom(from: String) {
    }
}

class FlipFlop(override val name: String) : Module() {
    var isOff = true

    override fun receiveAndEmit(pulse: Pulse): List<Pulse> {
        if (pulse.intensity == PulseIntensity.HIGH) {
            return emptyList()
        }

        isOff = !isOff
        val pi = if (isOff) PulseIntensity.LOW else PulseIntensity.HIGH
        return connectedTo.map { Pulse(name, it, pi) }
    }

    override fun connectedFrom(from: String) {
    }

}

class Conjunction(override val name: String) : Module() {
    val connectedFrom = mutableMapOf<String, PulseIntensity>()

    override fun receiveAndEmit(pulse: Pulse): List<Pulse> {
        connectedFrom[pulse.from] = pulse.intensity
        val pi = if (connectedFrom.values.all { it == PulseIntensity.HIGH }) {
            PulseIntensity.LOW
        } else {
            PulseIntensity.HIGH
        }

        return connectedTo.map { Pulse(name, it, pi) }
    }

    override fun connectedFrom(from: String) {
        connectedFrom[from] = PulseIntensity.LOW
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


    fun addModule(module: Module) {
        modules[module.name] = module
    }

    fun connect(from: String, to: String) {
        if (to == "") return
        if(! modules.containsKey(to)){
            println("adding output $to")
            addModule(Output(to))
        }
        modules[from]!!.connectedTo(to)
        modules[to]!!.connectedFrom(from)
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

    fun part2(input: List<String>): Int {

        return 42
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
