package day15

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class LensesTest {
    val steps = listOf(
        Lenses.empty(),
        Lenses.fill(
            listOf(
                0 to listOf(Lens("rn", 1))
            )
        ),
        Lenses.fill(
            listOf(
                0 to listOf(Lens("rn", 1))
            )
        ),
        Lenses.fill(
            listOf(
                0 to listOf(Lens("rn", 1)),
                1 to listOf(Lens("qp", 3)),
            )
        ),
        Lenses.fill(
            listOf(
                0 to listOf(Lens("rn", 1), Lens("cm", 2)),
                1 to listOf(Lens("qp", 3)),
            )
        ),
        Lenses.fill(
            listOf(
                0 to listOf(Lens("rn", 1), Lens("cm", 2)),
            )
        ),
    )

    fun testStep(i: Int, input: String) {
        val lenses = steps[i]
        val got = lenses.process(input)

        assertEquals(steps[i + 1], got)
    }

    @TestFactory
    fun testSteps() = listOf(
        0 to "rn=1",
        1 to "cm-",
        2 to "qp=3",
        3 to "cm=2",
        4 to "qp-"
    ).map { (iStep: Int, input: String) ->
        DynamicTest.dynamicTest(" $iStep ($input)") {
            testStep(iStep, input)
        }
    }

    @Test
    fun testScore() {
        val given = Lenses.fill(
            listOf(
                0 to listOf(Lens("rn", 1), Lens("cm", 2)),
                3 to listOf(Lens("ot", 7), Lens("ab", 5), Lens("pc", 6)),
            )
        )
        val got = given.score()
        assertEquals(145, got)
    }
}