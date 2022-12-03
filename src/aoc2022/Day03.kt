package aoc2022

import fetch
import letterIndex

fun main() {
    Day03.part1()
    Day03.part2()
}

object Day03 {

    private val input = fetch(2022, 3).lines()

    fun part1() {
        val sum = input.sumOf { line ->
            // Split each line/rucksack into two halves
            val firstHalf = line.substring(0, line.length / 2)
            val secondHalf = line.substring(line.length / 2, line.length)
            // Find the common character between the first and second half of the line (rucksack)
            val common = firstHalf.find { secondHalf.contains(it) }!!
            // Add the priority of this character to the sum
            return@sumOf getPriority(common)
        }
        println("Part 1 result: $sum")
    }

    fun part2() {
        val sum = input
            .chunked(3) // Group the input into chunks of 3 lines.
                             // For example: [1, 2, 3, 4, 5, 6] => [[1, 2, 3], [4, 5, 6]]
            .sumOf { group ->
                // Find the common letter between all 3
                val common = group[0]
                    .filter { group[1].contains(it) && group[2].contains(it) } // Filter characters from the first line that the next two lines also have
                    .toList()
                    .distinct() // Duplicated elements of the same letter are ignored
                    .single() // Pick one and only one element from this list; and more is an error
                // Add the priority of this character to the sum
                return@sumOf getPriority(common)
            }
        println("Part 2 result: $sum")
    }

    private fun getPriority(char: Char) = if (char.isUpperCase()) {
        char.letterIndex + 1 + 27 // Uppercase letters range from 27 to 53, inclusive
    } else {
        char.letterIndex + 1 // Lowercase letters range from 1 to 16, inclusive
    }
}