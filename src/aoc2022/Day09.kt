package aoc2022

import Day
import kotlin.math.abs
import kotlin.math.max

object Day09 : Day(2022, 9) {

    private lateinit var knots: MutableList<Pair<Int, Int>>
    private val visited = mutableSetOf<Pair<Int, Int>>()

    override fun part1() {
        // Part 1 uses 2 knots (a head and a tail)
        knots = MutableList(2) { 0 to 0 }
        simulate()
        println("Part 1 result: ${visited.size}")
    }

    override fun part2() {
        // Part 2 uses a rope with 10 knots
        knots = MutableList(10) { 0 to 0 }
        simulate()
        println("Part 2 result: ${visited.size}")
    }

    private fun simulate() {
        visited.clear()
        visited.add(knots[knots.size - 1]) // Make sure the starting position is counted
        for (instruction in lines) {
            if (instruction.isEmpty()) continue // Last line may be empty
            val (direction, amountStr) = instruction.split(" ")
            val amount = amountStr.toInt()

            for (i in 0 until amount) {
                val head = knots[0]
                knots[0] = when (direction) {
                    "L" -> head.copy(first = head.first - 1) // Move left
                    "R" -> head.copy(first = head.first + 1) // Move right
                    "U" -> head.copy(second = head.second + 1) // Move up
                    "D" -> head.copy(second = head.second - 1) // Move down
                    else -> error("Invalid move: $direction")
                }
                updateTail()
            }
        }
    }

    private fun updateTail() {
        // Convert the list of knots into a set of windows. For example:
        // [A, B, C, D] => [[A, B], [B, C], [C, D]]
        for ((nextIndex, prevIndex) in knots.indices.windowed(size = 2, step = 1, partialWindows = false)) {
            // Get the coordinates of the previous and next knots
            val (hx, hy) = knots[nextIndex]
            val (tx, ty) = knots[prevIndex]

            // Don't update if the previous knot is next to the next knot
            if (abs(hx - tx) <= 1 && abs(hy - ty) <= 1) continue

            // Move this knot up, down, left, or right in the direction of the next knot in the line.
            knots[prevIndex] = when {
                hx - tx == -2 && hy == ty -> knots[prevIndex].copy(first = tx - 1)
                hx - tx == 2 && hy == ty -> knots[prevIndex].copy(first = tx + 1)
                hy - ty == 2 && hx == tx -> knots[prevIndex].copy(second = ty + 1)
                hy - ty == -2 && hx == tx -> knots[prevIndex].copy(second = ty - 1)
                else -> knots[prevIndex] // No straight-line move was necessary
            }

            // Move this knot diagonally based on its relative position to the next knot.
            val dx = hx - tx
            val dy = hy - ty

            if (max(abs(dx), abs(dy)) == 2 && dx != 0 && dy != 0) {
                val yAdj = if (dy > 0) knots[prevIndex].up() else knots[prevIndex].down()
                val adj = if (dx > 0) yAdj.right() else yAdj.left()
                knots[prevIndex] = adj
            }

            // Make sure the knot moved into the proper place. If not, the program's output could be incorrect!
            if (abs(knots[prevIndex].first - knots[nextIndex].first) > 1 ||
                abs(knots[prevIndex].second - knots[nextIndex].second) > 1
            ) {
                printGrid()
                error("Unable to properly adjust tail!")
            }

            // Add the tail end of the rope to the [visited] set (there are no duplicate elements in a Set)
            visited.add(knots[knots.size - 1])
        }
    }

    private fun Pair<Int, Int>.down() = copy(second = second - 1)
    private fun Pair<Int, Int>.up() = copy(second = second + 1)
    private fun Pair<Int, Int>.left() = copy(first = first - 1)
    private fun Pair<Int, Int>.right() = copy(first = first + 1)

    /**
     * Prints the current grid of [knots] to the standard output.
     * The size of the grid depends on the positions of all knots.
     */
    private fun printGrid() {
        if (visited.isEmpty()) {
            println("(Empty grid)")
            return
        }

        val minX = knots.minOf { it.first }.coerceAtMost(0)
        val minY = knots.minOf { it.second }.coerceAtMost(0)
        val maxX = knots.maxOf { it.first }.coerceAtLeast(0)
        val maxY = knots.maxOf { it.second }.coerceAtLeast(0)

        for (y in maxY downTo minY) {
            for (x in minX..maxX) {
                val knot = knots.indexOfFirst { it.first == x && it.second == y }
                if (knot != -1) print(knot.label()) else print(".")
            }
            println()
        }
    }

    private fun Int.label() = if (this == 0) "H" else if (this == knots.size - 1) "T" else this.toString()
}