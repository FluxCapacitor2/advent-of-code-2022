package aoc2022

import Day
import splitOn

object Day01 : Day(2022, 1) {

    // Split the input based on blank lines between sets of numbers
    private val sets = lines
        .map { it.toIntOrNull() }
        .splitOn { it == null }
        .map { it.requireNoNulls() } // The `sum` function doesn't allow potentially-nullable elements

    override fun part1() {
        // Sum each set of numbers and find the greatest
        val greatest = sets.maxOf { it.sum() }
        // Print the result
        println("Part 1 result: $greatest")
    }

    override fun part2() {
        val greatestThree = sets
            .sortedByDescending { it.sum() } // Sort the sets by their sum
            .take(3) // Take the top three
            .sumOf { it.sum() } // Calculate the total amount of calories
        println("Part 2 result: $greatestThree")
    }
}