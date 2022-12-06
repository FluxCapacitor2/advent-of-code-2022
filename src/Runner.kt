import java.util.*

fun main() {
    val cal = Calendar.getInstance()
    val className = if (cal.get(Calendar.MONTH) == 11) {
        "aoc${cal.get(Calendar.YEAR)}.Day${cal.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')}"
    } else {
        println("It's not December! Input the year and day to run an Advent of Code day.")
        println("Year: ")
        val year = readln().toInt()
        println("Day: ")
        val day = readln().toInt()
        "aoc$year.Day${day.toString().padStart(2, '0')}"
    }

    println("Running day: $className")
    val day = Class.forName(className).kotlin.objectInstance as Day
    day.run()
}