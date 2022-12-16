package aoc2022

import Day
import java.lang.Integer.max
import java.lang.Integer.min

object Day14 : Day(2022, 14) {

    private fun pointsBetween(one: Point, two: Point): List<Point> {
        val minX = min(one.x, two.x)
        val minY = min(one.y, two.y)
        val maxX = max(one.x, two.x)
        val maxY = max(one.y, two.y)
        return (minX..maxX).flatMap { x ->
            (minY..maxY).map { y ->
                x to y
            }
        }
    }

    private fun points() = lines
        .filter { it.isNotBlank() }
        // Split each line into lists of points
        .map { line ->
            line.split(" -> ")
                // For every point, convert it into a Pair<Int, Int>
                .map { pair ->
                    pair.split(",").run {
                        get(0).toInt() to get(1).toInt()
                    }
                }
        }.flatMap { pointList ->
            // Connect those points by filling in the gaps
            pointList.windowed(2, 1, false).flatMap {
                pointsBetween(it[0], it[1])
            }
        }

    private val points = points()

    override fun part1() {
        val grid = points.toHashSet()

        // Get min and max values for all coordinates at the start of the process
        // These are used to check if sand is outside the initial bounds of the 'simulation'
        val minX = points.minOf { it.x }
        val minY = 0
        val maxX = points.maxOf { it.x }
        val maxY = points.maxOf { it.y }

        // Simulate snowfall
        val start = 500 to 0
        var currentPos: Point = start
        var unitsOfSand = 0
        while (true) {
            val down = currentPos.copy(second = currentPos.y + 1)
            val downLeft = currentPos.copy(first = currentPos.x - 1, second = currentPos.y + 1)
            val downRight = currentPos.copy(first = currentPos.x + 1, second = currentPos.y + 1)

            val newPos = if (!grid.contains(down)) down
            else if (!grid.contains(downLeft)) downLeft
            else if (!grid.contains(downRight)) downRight
            else {
                // The snow has come to rest
                unitsOfSand++
                currentPos = start
                continue
            }

            // The snow has moved during this iteration
            // If it's outside the bounds, then we've processed the final unit of sand
            grid.add(newPos)
            grid.remove(currentPos)
            currentPos = newPos
            if (newPos.x > maxX || newPos.x < minX || newPos.y > maxY || newPos.y < minY) {
                println("Part 1 result: $unitsOfSand")
                break
            }
        }
    }

    override fun part2() {

        val grid = points.toHashSet()

        val maxY = grid.maxOf { it.y }

        // Simulate snowfall
        val start = 500 to 0
        var currentPos: Point = start
        var unitsOfSand = 0
        while (true) {
            val down = currentPos.copy(second = currentPos.y + 1)
            val downLeft = currentPos.copy(first = currentPos.x - 1, second = currentPos.y + 1)
            val downRight = currentPos.copy(first = currentPos.x + 1, second = currentPos.y + 1)

            val newPos = if (!grid.contains(down) && down.y < maxY + 2) down
            else if (!grid.contains(downLeft) && down.y < maxY + 2) downLeft
            else if (!grid.contains(downRight) && down.y < maxY + 2) downRight
            else {
                // The snow has come to rest
                unitsOfSand++
                if (currentPos == start) {
                    // The final unit of sand has been processed
                    println("Part 2 result: $unitsOfSand")
                    break
                }
                currentPos = start
                continue
            }

            grid.add(newPos)
            grid.remove(currentPos)
            currentPos = newPos
        }
    }
}

private typealias Point = Pair<Int, Int>

private val Point.x get() = first
private val Point.y get() = second