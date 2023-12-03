import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/**
 * takes a list of string as input and returns a list of list of booleam, by applying a function to each character
 */
fun List<String>.mapToBooleanList(f: (Char) -> Boolean): List<List<Boolean>> =
    this.map { it.map(f) }

fun <S, T> List<List<T>>.mapMatrix(f: (T) -> S): List<List<S>> =
    this.map { it.map(f) }


/** With a matrix of boolean, apply a lateral shift of 1 to the right, insert a given value on the left, and return the new matrix */
fun <T> List<List<T>>.shiftE(value: T): List<List<T>> =
    this.map { listOf(value).plus(it.dropLast(1)) }

fun <T> List<List<T>>.shiftW(value: T): List<List<T>> =
    this.map { it.drop(1).plus(value) }

fun <T> List<List<T>>.shiftS(value: T): List<List<T>> {
    val row = listOf((1..this[0].size).map { value })
    return row.plus(this.dropLast(1))
}

fun <T> List<List<T>>.shiftN(value: T): List<List<T>> {
    val row = listOf((1..this[0].size).map { value })
    return this.drop(1).plus(row)
}

fun List<List<Boolean>>.toString(c: Char = 'X'): String {
    return this.map { it.map { if (it) c else "." }.joinToString("") }.joinToString("\n")
}

fun <S, T, U> List<List<S>>.zipApply(
    other: List<List<T>>,
    f: (a: S, b: T) -> U
): List<List<U>> =
    this.zip(other).map { (a, b) -> a.zip(b).map { (x, y) -> f(x, y) } }

fun List<List<Boolean>>.or(other: List<List<Boolean>>): List<List<Boolean>> =
    this.zipApply(other) { a, b -> a || b }

fun List<List<Boolean>>.and(other: List<List<Boolean>>): List<List<Boolean>> =
    this.zipApply(other) { a, b -> a && b }

fun <T> List<List<T>>.mask(other: List<List<Boolean>>, default: T): List<List<T>> =
    this.zip(other).map { (ra, rb) ->
        ra.zip(rb).map { (a, b) ->
            if (b) {
                a
            } else {
                default
            }
        }
    }


