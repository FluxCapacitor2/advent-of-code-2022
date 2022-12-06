package aoc2022

import Day

object Day06 : Day(2022, 6) {
    override fun part1() {
        val windows = input.windowed(4, 1, false)
        for ((i, window) in windows.withIndex()) {
            if(window.toCharArray().distinct().size == window.length) {
                println("Part 1 result: ${i + window.length}")
                break
            }
        }
    }

    override fun part2() {
        val windows = input.windowed(14, 1, false)
        for ((i, window) in windows.withIndex()) {
            if(window.toCharArray().distinct().size == window.length) {
                println("Part 2 result: ${i + window.length}")
                break
            }
        }
    }

}