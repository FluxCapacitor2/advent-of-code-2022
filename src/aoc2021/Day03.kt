package aoc2021

import column
import columns
import fetch
import leastCommonChar
import mostCommonChar

fun main() {
    Day03.part1()
    Day03.part2()
}

object Day03 {

    private val input = fetch(2021, 3).lines()

    fun part1() {
        var gamma = ""
        var epsilon = ""
        for (column in input.columns()) {
            // The most and least common characters are added to gamma and epsilon.
            gamma += column.mostCommonChar()
            epsilon += column.leastCommonChar()
        }
        // Convert gamma and epsilon to integers, from binary.
        val gammaRate = gamma.toLong(2)
        val epsilonRate = epsilon.toLong(2)
        // Multiply the two numbers to get the result.
        val result = gammaRate * epsilonRate
        println("Part 1 result: $result")
    }

    fun part2() {
        // Oxygen generator rating
        val oxygenGeneratorRating: Int = applyFilter(input) {
            // Find the most common character. If there's a tie, return 1.
            if (it.mostCommonChar() == it.leastCommonChar()) '1' else it.mostCommonChar()
        }.single().toInt(2)

        // CO2 scrubber rating
        val co2ScrubberRating: Int = applyFilter(input) {
            // Find the least common character. If there's a tie, return 0.
            if (it.mostCommonChar() == it.leastCommonChar()) '0' else it.leastCommonChar()
        }.single().toInt(2)

        val result = oxygenGeneratorRating * co2ScrubberRating
        println("Part 2 result: $result")
    }

    private fun applyFilter(input: List<String>, columnIndex: Int = 0, toKeep: (String) -> Char): List<String> {
        val column = input.column(columnIndex)
        val result = input.filter {
            it[columnIndex] == toKeep(column)
        }
        return if (result.size == 1) result else {
            applyFilter(result, columnIndex + 1, toKeep)
        }
    }
}