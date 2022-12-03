package aoc2021

import fetch

fun main() {
    Day01.part1()
    Day01.part2()
}

object Day01 {

    private val input = fetch(2021, 1).lines().map { it.toInt() }

    fun part1() {
        val numIncreases = input.windowed(2, 1, false).count {
            it[1] > it[0] // Count the number of times that numbers in the list increase from the previous number
        }
        println("Part 1 result: $numIncreases")
    }

    fun part2() {
        val numIncreases = input
            .windowed(3, 1, false) // Split the data into sub-lists of length 3, which overlap 2 elements with the prev/next lists.
            .map {
                // Sum each sub-list
                it.sum()
            }
            .windowed(2, 1, false) // Split the sums into windows of 2 to compute the amount of increases
            .count {
                // Count the number of times the sums increased
                it[1] > it[0]
            }
        println("Part 2 result: $numIncreases")
    }
}