import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Day02KtTest {
    @Test
    fun testReadOneDraw() {
        val given = "3 blue, 4 red"
        val got = readOneDraw(given)
        assertEquals(listOf(3, 4, 0, 0), got)
    }

    @Test
    fun testReadOneGame() {
        val given = "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"
        val got = readOneGame(given)
        assertEquals(listOf(listOf(3, 4, 0, 0), listOf(6, 1, 2, 0), listOf(0, 0, 2, 0)), got)

    }

}