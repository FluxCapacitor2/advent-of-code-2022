package aoc2022

import Day
import column

object Day08 : Day(2022, 8) {

    override fun part1() {
        var totalVisible = 0
        val input = lines.filter { it.isNotEmpty() }
        for ((row, line) in input.withIndex()) {
            for ((column, char) in line.withIndex()) {
                if (row == 0 || column == 0 || row == input.size - 1 || column == input[column].length - 1) {
                    // This tree is on the edge; it is visible
                    totalVisible++
                } else {
                    // If not, we have to check if there are any taller trees that block view from the edge
                    val columnString = input.column(column)

                    val isVisible = listOf(
                        line.substring(0, column), // Left
                        line.substring(column + 1), // Right
                        columnString.substring(0, row), // Above
                        columnString.substring(row + 1), // Below
                    ).any { !isObstructed(char, it) }

                    if (isVisible) totalVisible++
                }
            }
        }
        println("Part 1 result: $totalVisible")
    }

    private fun isObstructed(tree: Char, line: String): Boolean {
        for (char in line) {
            if (char.digitToInt() >= tree.digitToInt()) return true
        }
        return false
    }

    override fun part2() {
        var maxScenicScore = 0
        val input = lines.filter { it.isNotEmpty() }
        for ((row, line) in input.withIndex()) {
            for ((column, char) in line.withIndex()) {
                if (row == 0 || column == 0 || row == input.size - 1 || column == line.length - 1) {
                    // This tree is on the edge; one of the scenic scores must be zero, so nothing needs to be computed
                    continue
                }

                val columnString = input.column(column)

                // Compute the scenic score for each character
                val scores = listOf(
                    getVisibleTrees(columnString.substring(0, row).reversed(), char), // Above
                    getVisibleTrees(columnString.substring(row + 1), char), // Below
                    getVisibleTrees(line.substring(0, column).reversed(), char), // Left
                    getVisibleTrees(line.substring(column + 1), char) // Right
                )

                val total = scores.reduce { acc, i -> acc * i }

                maxScenicScore = total.coerceAtLeast(maxScenicScore)
            }
        }
        println("Part 2 result: $maxScenicScore")
    }

    private fun getVisibleTrees(list: String, char: Char): Int {
        if (list.isEmpty()) return 0
        for ((index, c) in list.withIndex()) {
            if (c.digitToInt() >= char.digitToInt()) {
                return index + 1
            }
        }
        return list.length
    }
}