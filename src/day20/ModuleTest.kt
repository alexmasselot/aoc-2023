package day20

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ModuleTest {
    @Nested
    inner class FlipFlopTest {
        private val flipFlop = FlipFlop("test")

        init {
            flipFlop.connectedTo("a")
            flipFlop.connectedTo("b")
        }

        @BeforeEach
        fun setUp() {
            flipFlop.isOff = true

        }

        @Test
        fun `should emit nothing on high pulse`() {
            val got = flipFlop.receiveAndEmit(Pulse("X", "test", PulseIntensity.HIGH))
            assertEquals(0, got.size)
        }

        @Test
        fun `with state off should flip on low pulse and send high`() {
            flipFlop.isOff = true
            val got = flipFlop.receiveAndEmit(Pulse("X", "test", PulseIntensity.LOW))
            assertEquals(2, got.size)
            assertEquals(Pulse("test", "a", PulseIntensity.HIGH), got[0])
            assertEquals(Pulse("test", "b", PulseIntensity.HIGH), got[1])
            assertFalse(flipFlop.isOff)
        }

        @Test
        fun `with state on should flip on low pulse and send low`() {
            flipFlop.isOff = false
            val got = flipFlop.receiveAndEmit(Pulse("X", "test", PulseIntensity.LOW))
            assertEquals(2, got.size)
            assertEquals(Pulse("test", "a", PulseIntensity.LOW), got[0])
            assertEquals(Pulse("test", "b", PulseIntensity.LOW), got[1])
            assertTrue(flipFlop.isOff)
        }
    }

    @Nested
    inner class ConjunctionTest {
        private val conjunction = Conjunction("test")

        init {
            conjunction.connectedTo("a")
            conjunction.connectedTo("b")
        }

        @BeforeEach
        fun setUp() {
            // adding connected from also reset memory to LOW
            conjunction.connectFrom("x")
            conjunction.connectFrom("y")
            conjunction.connectFrom("z")
        }

        @Test
        fun `should emit HIGH when all memory are not HIGH`() {
            conjunction.connectedFromPulses["x"] = PulseIntensity.LOW
            conjunction.connectedFromPulses["y"] = PulseIntensity.LOW
            conjunction.connectedFromPulses["z"] = PulseIntensity.HIGH

            val got = conjunction.receiveAndEmit(Pulse("X", "test", PulseIntensity.HIGH))

            assertEquals(2, got.size)
            assertEquals(Pulse("test", "a", PulseIntensity.HIGH), got[0])
            assertEquals(Pulse("test", "b", PulseIntensity.HIGH), got[1])

        }

        @Test
        fun `should emit LOW when all memory are  HIGH and receives HIGH`() {
            conjunction.connectedFromPulses["x"] = PulseIntensity.HIGH
            conjunction.connectedFromPulses["y"] = PulseIntensity.HIGH
            conjunction.connectedFromPulses["z"] = PulseIntensity.HIGH

            val got = conjunction.receiveAndEmit(Pulse("X", "test", PulseIntensity.HIGH))

            assertEquals(2, got.size)
            assertEquals(Pulse("test", "a", PulseIntensity.LOW), got[0])
            assertEquals(Pulse("test", "b", PulseIntensity.LOW), got[1])
        }

        @Test
        fun `should emit LOW when all memory are HIGH but the source and receives HIGH`() {
            conjunction.connectedFromPulses["x"] = PulseIntensity.LOW
            conjunction.connectedFromPulses["y"] = PulseIntensity.HIGH
            conjunction.connectedFromPulses["z"] = PulseIntensity.HIGH

            val got = conjunction.receiveAndEmit(Pulse("X", "test", PulseIntensity.HIGH))

            assertEquals(2, got.size)
            assertEquals(Pulse("test", "a", PulseIntensity.HIGH), got[0])
            assertEquals(Pulse("test", "b", PulseIntensity.HIGH), got[1])
        }
    }

    @Nested
    inner class ModuleConfigTest {
        @Nested
        inner class Example1 {
            val input = """
                broadcaster -> a, b, c
                %a -> b
                %b -> c
                %c -> inv
                &inv -> a
            """.trimIndent().lines()

            @Test
            fun parse() {
                val mc = ModuleConfig()
                mc.parse(input)
                assertEquals(5, mc.modules.size)
                assertTrue(mc.modules["broadcaster"] is Broadcaster)
                assertTrue(mc.modules["a"] is FlipFlop)
                assertTrue(mc.modules["b"] is FlipFlop)
                assertTrue(mc.modules["c"] is FlipFlop)
                assertTrue(mc.modules["inv"] is Conjunction)
            }

            @Test
            fun pressButton() {
                val mc = ModuleConfig()
                mc.parse(input)
                mc.pressButton()
                assertEquals(1, mc.fifo.size)
                assertEquals("button -low-> broadcaster", mc.fifo[0].toString())
            }

            @Test
            fun step1() {
                val mc = ModuleConfig()
                mc.parse(input)
                mc.pressButton()
                mc.step()
                assertEquals(3, mc.fifo.size)
                assertEquals("broadcaster -low-> a", mc.fifo[0].toString())
                assertEquals("broadcaster -low-> b", mc.fifo[1].toString())
                assertEquals("broadcaster -low-> c", mc.fifo[2].toString())
            }

            @Test
            fun testAndCollect() {
                val mc = ModuleConfig()
                mc.parse(input)
                val got = mc.pressAndCollect()

                val expected = """
                    button -low-> broadcaster
                    broadcaster -low-> a
                    broadcaster -low-> b
                    broadcaster -low-> c
                    a -high-> b
                    b -high-> c
                    c -high-> inv
                    inv -low-> a
                    a -low-> b
                    b -low-> c
                    c -low-> inv
                    inv -high-> a
                """.trimIndent()

                assertEquals(expected, got.joinToString("\n"))
            }
        }

        @Nested
        inner class Example2 {
            val input = """
                broadcaster -> a
                %a -> inv, con
                &inv -> b
                %b -> con
                &con -> output
            """.trimIndent().lines()

            @Test
            fun multiplePressAndCollect(){
                val mc = ModuleConfig()
                mc.parse(input)

                fun pc() =
                    mc.pressAndCollect().joinToString("\n")

                assertEquals(
                    """                    
                button -low-> broadcaster
                broadcaster -low-> a
                a -high-> inv
                a -high-> con
                inv -low-> b
                con -high-> output
                b -high-> con
                con -low-> output
                """.trimIndent(), pc()
                )
                assertEquals(
                    """
                 button -low-> broadcaster
                 broadcaster -low-> a
                 a -low-> inv
                 a -low-> con
                 inv -high-> b
                 con -high-> output
                """.trimIndent(), pc()
                )
                assertEquals(
                    """
                button -low-> broadcaster
                broadcaster -low-> a
                a -high-> inv
                a -high-> con
                inv -low-> b
                con -low-> output
                b -low-> con
                con -high-> output
                """.trimIndent(), pc()
                )
                assertEquals(
                    """
                        button -low-> broadcaster
                        broadcaster -low-> a
                        a -low-> inv
                        a -low-> con
                        inv -high-> b
                        con -high-> output
                    """.trimIndent(), pc()
                )
            }
        }
    }
}