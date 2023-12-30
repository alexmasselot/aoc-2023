package day22

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class BitMatrixTest {
    @Test
    fun `check cols are constructed at init`() {
        val given = BitMatrix(
            listOf(
                BitSet.valueOf(longArrayOf(0b0110)),
                BitSet.valueOf(longArrayOf(0b1110)),
                BitSet.valueOf(longArrayOf(0b0001)),
                BitSet.valueOf(longArrayOf(0b0101)),
            )
        )

        val got = given.cols
        assertEquals(4, got.size)
        assertEquals(BitSet.valueOf(longArrayOf(0b1100)), got[0])
        assertEquals(BitSet.valueOf(longArrayOf(0b0011)), got[1])
        assertEquals(BitSet.valueOf(longArrayOf(0b1011)), got[2])
        assertEquals(BitSet.valueOf(longArrayOf(0b0010)), got[3])

    }

    @Test
    fun `multiply 3x3`(){
        val a = BitMatrix(
            listOf(
                BitSet.valueOf(longArrayOf(0b011)),
                BitSet.valueOf(longArrayOf(0b001)),
                BitSet.valueOf(longArrayOf(0b010)),
            )
        )
        val b = BitMatrix(
            listOf(
                BitSet.valueOf(longArrayOf(0b010)),
                BitSet.valueOf(longArrayOf(0b000)),
                BitSet.valueOf(longArrayOf(0b110)),
            )
        )
        val expected = BitMatrix(
            listOf(
                BitSet.valueOf(longArrayOf(0b010)),
                BitSet.valueOf(longArrayOf(0b010)),
                BitSet.valueOf(longArrayOf(0b000)),
            )
        )

        val got = a.multiply(b)

        assertEquals(expected, got)

    }
}