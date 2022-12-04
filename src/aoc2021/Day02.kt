package aoc2021

import Day

object Day02 : Day(2021, 2) {

    /* Test case:
    private val input = """
        forward 5
        down 5
        forward 8
        up 3
        down 8
        forward 2
    """.trimIndent().lines()
    Should produce "150" as the result. (15 * 10)
     */

    override fun part1() {
        var horizontalPos = 0
        var depth = 0
        lines.forEach { line ->
            val num = line.substringAfter(' ').toInt()
            when (line.substringBefore(' ')) {
                "forward" -> horizontalPos += num
                "up" -> depth -= num
                "down" -> depth += num
                else -> error("Unexpected instruction in line: $line")
            }
        }
        val result = horizontalPos * depth
        println("Part 1 result: $result")
    }

    override fun part2() {
        var horizontalPos = 0
        var depth = 0
        var aim = 0
        lines.forEach { line ->
            val num = line.substringAfter(' ').toInt()
            when (line.substringBefore(' ')) {
                "forward" -> {
                    horizontalPos += num
                    depth += aim * num
                }

                "up" -> aim -= num
                "down" -> aim += num

                else -> error("Unexpected instruction in line: $line")
            }
        }
        val result = horizontalPos * depth
        println("Part 2 result: $result")
    }
}