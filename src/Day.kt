import kotlin.system.measureTimeMillis

abstract class Day(private val year: Int, private val day: Int) {

    val input = fetch(year, day)
    val lines by lazy {
        input.lines()
    }

    abstract fun part1()

    abstract fun part2()

    fun run() {
        println("Running Advent of Code $year day $day...")

        measureTimeMillis {
            part1()
        }.let { ms -> println("Finished running part 1 in ${ms}ms.") }

        measureTimeMillis {
            part2()
        }.let { ms -> println("Finished running part 2 in ${ms}ms.") }
    }

}