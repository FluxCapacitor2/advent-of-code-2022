import java.net.URL
import java.nio.file.Paths
import java.util.function.Predicate
import kotlin.io.path.*

/**
 * Fetches input data from the AoC website for a given [year] and [day] of December.
 * Requires the session cookie to be placed in `inputs/session.txt` because inputs
 * are different per-user. If the inputs have already been downloaded, they will
 * be returned without downloading them.
 */
fun fetch(year: Int, day: Int): String {
    val file = Paths.get("inputs", "${year}_${day.toString().padStart(2, '0')}.txt")
    if (file.exists()) {
        return file.readText().trim()
    }
    val url = URL("https://adventofcode.com/$year/day/$day/input")
    println("Downloading $url...")
    try {
        val contents = httpRequest(url, Paths.get("inputs", "session.txt").readText())
        file.createFile()
        file.writeText(contents)
        return contents.trim()
    } catch (e: Throwable) {
        throw IllegalStateException(
            "Download failed. Please update your session token or download the data manually from: $url",
            e
        )
    }
}

/**
 * Splits the [Iterable] whenever the [predicate] returns true
 * on one of the items in the Iterable. If the predicate returns
 * true for an item, it is *not* included in the final list.
 *
 * Example: `[1, 2, 3, 4, 5, 6, 7, 8, 9, 10].splitOn { it % 3 == 0}`
 * results in `[[1, 2], [4, 5], [7, 8], [10]]`.
 */
fun <T> Iterable<T>.splitOn(predicate: Predicate<T>): MutableList<List<T>> {
    val list = toList()

    val temp = mutableListOf<T>()
    val done = mutableListOf<List<T>>()

    list.forEach { item ->
        if (predicate.test(item)) {
            done.add(temp.toList())
            temp.clear()
        } else {
            temp.add(item)
        }
    }

    if (temp.isNotEmpty())
        done.add(temp.toList())

    return done
}

/**
 * Switches the dimensions of a two-dimensional List
 * and returns its columns.
 */
fun <T> List<List<T>>.columns(): List<List<T>> {
    return get(0).indices.map { i ->
        map { row -> row[i] }
    }
}

fun Iterable<String>.columns(): List<String> {
    return toList()
        .map { it.toCharArray().toList() }
        .columns()
        .map { String(it.toCharArray()) }
}

fun List<String>.column(index: Int): String {
    return map { row -> row[index] }.concatToString()
}

fun <T> List<T>.takeAllAfter(index: Int): List<T> {
    return takeLast(size - index)
}

private fun httpRequest(url: URL, cookie: String): String {
    val conn = url.openConnection()
    conn.setRequestProperty("Cookie", cookie)
    conn.setRequestProperty("User-Agent", "github.com/FluxCapacitor2/advent-of-code")
    return conn.getInputStream().readBytes().toString(Charsets.UTF_8)
}