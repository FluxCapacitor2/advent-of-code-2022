import java.net.URL
import java.nio.file.Paths
import kotlin.io.path.createFile
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

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
        throw IllegalStateException("Download failed. Please update your session token or download the data manually from: $url", e)
    }
}

private fun httpRequest(url: URL, cookie: String): String {
    val conn = url.openConnection()
    conn.setRequestProperty("Cookie", cookie)
    conn.setRequestProperty("User-Agent", "github.com/FluxCapacitor2/advent-of-code")
    return conn.getInputStream().readBytes().toString(Charsets.UTF_8)
}