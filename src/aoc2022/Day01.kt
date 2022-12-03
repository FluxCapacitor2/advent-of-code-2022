package aoc2022

import fetch
import splitOn

fun main() {
    Day01.part1()
    Day01.part2()
}

object Day01 {

    // Read input from a file
    private val input = fetch(2022, 1).lines()

    // Split the input based on blank lines between sets of numbers
    private val sets = input
        .map { it.toIntOrNull() }
        .splitOn { it == null }
        .map { it.requireNoNulls() } // The `sum` function doesn't allow potentially-nullable elements

    fun part1() {
        // Sum each set of numbers and find the greatest
        val greatest = sets.maxOf { it.sum() }
        // Print the result
        println("Part 1 result: $greatest")
    }

    fun part2() {
        val greatestThree = sets
            .sortedByDescending { it.sum() } // Sort the sets by their sum
            .take(3) // Take the top three
            .sumOf { it.sum() } // Calculate the total amount of calories
        println("Part 2 result: $greatestThree")
    }
}