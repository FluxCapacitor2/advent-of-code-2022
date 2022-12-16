import java.io.File
import kotlin.system.measureTimeMillis

abstract class Day(private val year: Int, private val day: Int) {

    /**
     * Regular (large) puzzle input
     */
    val input = fetch(year, day)

    /**
     * Sample (smaller) puzzle input for testing purposes
     */
    private val sample by lazy {
        File("inputs/sample.txt").readLines()
    }
    private var useSample = false
    protected fun useSample() {
        useSample = true
    }

    val lines: List<String> get() {
        return if (useSample) sample
        else input.lines()
    }

    abstract fun part1()

    abstract fun part2()

    fun run() {
        println("Running Advent of Code $year day $day...")

        measureTimeMillis {
            part1()
        }.let { ms -> println("Finished running part 1 in ${ms}ms.") }
        useSample = false
        measureTimeMillis {
            part2()
        }.let { ms -> println("Finished running part 2 in ${ms}ms.") }
    }

}