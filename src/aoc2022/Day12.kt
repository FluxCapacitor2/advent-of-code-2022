package aoc2022

import Day

object Day12 : Day(2022, 12) {

    private const val printGrids = false // Set to true for a visualization of the final paths.

    override fun part1() {
        val grid = lines.map { it.split("").filter { line -> line.isNotBlank() } }.filter { it.isNotEmpty() }
        val start = findGridItems(grid, 'S').single()
        val distance = route(grid, start)
        println("Part 1 result: $distance")
    }

    override fun part2() {
        val grid = lines.map { it.split("").filter { line -> line.isNotBlank() } }.filter { it.isNotEmpty() }
        val starts = findGridItems(grid, 'a') + findGridItems(grid, 'S').single()
        val result = starts.minOf { loc ->
            route(grid, loc) ?: Integer.MAX_VALUE
        }
        println("Part 2 result: $result")
    }

    private fun printGrid(grid: List<List<String>>, visited: List<Pair<Int, Int>>) {
        var str = ""
        grid.forEachIndexed { r, row ->
            row.forEachIndexed { c, col ->
                str += if (visited.any { it.first == r && it.second == c }) col
                else "."
            }
            str += "\n"
        }
        println(str)
    }

    private data class Move(val steps: Int, val x: Int, val y: Int)

    private fun route(
        grid: List<List<String>>,
        start: Pair<Int, Int>
    ): Int? {

        val queue = mutableListOf<Move>()
        val visited = mutableListOf<Pair<Int, Int>>()
        val traceBack = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()

        queue.add(Move(0, start.first, start.second))
        visited.add(start)

        while (queue.isNotEmpty()) {

            val (steps, row, col) = queue.first()
            queue.removeFirst()
            visited.add(row to col)

            for (move in getSurrounding(grid, row to col, visited)) {
                if (!visited.contains(move)) {
                    // Make sure the move is valid
                    val nextItem = grid[move.first][move.second][0]
                    val currentItem = grid[row][col][0]

                    if (nextItem == 'E') {
                        if (currentItem == 'z' || currentItem == 'y') { // 'E' is the same elevation as 'z'
                            if (printGrids) {
                                // print trace-back
                                var currentTrace = traceBack[row to col]
                                val traced = mutableListOf<Pair<Int, Int>>()
                                traced.add(row to col)
                                while (true) {
                                    if (currentTrace != null) {
                                        traced.add(currentTrace)
                                    } else break
                                    currentTrace = traceBack[currentTrace]
                                }
                                printGrid(grid, traced)
                            }
                            return steps + 1
                        } else continue
                    }
                    if (currentItem == 'S' && nextItem != 'a') continue
                    if (nextItem - currentItem > 1 && currentItem != 'S') continue

                    queue.add(Move(steps + 1, move.first, move.second))
                    visited.add(move)
                    traceBack[move] = row to col
                }
            }
        }

        return null // No route was found after searching every possible combination.
    }

    private fun getSurrounding(
        grid: List<List<String>>,
        point: Pair<Int, Int>,
        visited: List<Pair<Int, Int>>,
    ): List<Pair<Int, Int>> {
        return listOf(
            point.first + 1 to point.second, // down
            point.first - 1 to point.second, // up
            point.first to point.second + 1, // right
            point.first to point.second - 1, // left
        ).filter {
            !visited.contains(it) && grid.getOrNull(it.first)?.getOrNull(it.second) != null
        }
    }

    private fun findGridItems(grid: List<List<String>>, char: Char): List<Pair<Int, Int>> {
        val items = mutableListOf<Pair<Int, Int>>()
        for ((rowIndex, row) in grid.withIndex()) {
            for ((columnIndex, col) in row.withIndex()) {
                if (col[0] == char) items.add(rowIndex to columnIndex)
            }
        }
        if (items.isEmpty()) {
            error("Grid item '$char' not found!")
        }
        return items
    }
}