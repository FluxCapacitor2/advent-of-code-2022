package aoc2022

import Day
import intersect

object Day03 : Day(2022, 3) {

    override fun part1() {
        val sum = lines.sumOf { line ->
            // Split each line/rucksack into two halves
            val (firstHalf, secondHalf) = line.chunked(line.length / 2)
            // Find the common character between the first and second half of the line (rucksack)
            val intersection = firstHalf intersect secondHalf
            val commonChar = intersection.single()
            // Add the priority of this character to the sum
            return@sumOf commonChar.priority
        }
        println("Part 1 result: $sum")
    }

    override fun part2() {
        val sum = lines
            .chunked(3) // Group the input into chunks of 3 lines.
                             // For example: [1, 2, 3, 4, 5, 6] => [[1, 2, 3], [4, 5, 6]]
            .sumOf { group ->
                // Find the common letter between all 3
                val intersection = group[0] intersect group[1] intersect group[2]
                val commonChar = intersection.distinct().single()
                // Add the priority of this character to the sum
                return@sumOf commonChar.priority
            }
        println("Part 2 result: $sum")
    }

    private val Char.priority
        get() = if (isUpperCase()) {
            this - 'A' + 26
        } else {
            this - 'a'
        }
}