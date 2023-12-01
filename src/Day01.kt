fun extractDigits(input: String): List<Int> {
    val wordToDigit = mapOf(
        "zero" to "0",
        "one" to "1",
        "two" to "2",
        "three" to "3",
        "four" to "4",
        "five" to "5",
        "six" to "6",
        "seven" to "7",
        "eight" to "8",
        "nine" to "9",
        "0" to "0",
        "1" to "1",
        "2" to "2",
        "3" to "3",
        "4" to "4",
        "5" to "5",
        "6" to "6",
        "7" to "7",
        "8" to "8",
        "9" to "9"
    )

    val regex = Regex("(" + wordToDigit.keys.joinToString("|") + ")")
    return wordToDigit.entries.flatMap { (text, v) ->
        Regex(text).findAll(input).map { it.range.first to v }
    }
        .sortedBy { it.first }
        .map { it.second.toInt() }
        .toList()

}

fun main() {
    // Locate digit in a string and return the first and the last ones concatenated

    fun locateDigit(input: String): String {
        val digits = input.filter { it.isDigit() }
        return "${digits.first()}${digits.last()}"
    }

    // locate digit written in words in a string and return the first and the last ones concatenated
    fun locateDigitInWords(input: String): String {
        val digits = extractDigits(input)
        return if (digits.isNotEmpty()) "${digits.first()}${digits.last()}" else ""
    }

    fun part1(input: List<String>): Int {
        return input.map { locateDigit(it).toInt() }.sum()
    }

    fun part2(input: List<String>): Int {

        return input.map { locateDigitInWords(it).toInt() }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day01/part1_test")
    check(part1(testInput) == 142)
    val testInput2 = readInput("day01/part2_test")
    check(part2(testInput2) == 281)

    val input = readInput("day01/part1")
    part1(input).println()
    part2(input).println()
}
