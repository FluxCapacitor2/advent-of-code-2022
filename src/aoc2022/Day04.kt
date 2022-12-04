package aoc2022

import Day

object Day04 : Day(2022, 4) {

    private val split = lines
        .map { it.split(",") } // Split each line by comma

    private val ranges = split.map { rangeSet -> // For each line, convert both of its ranges ("x-y") into an IntRange from x to y.
        rangeSet.map { rangeString ->
            val s = rangeString.split("-")
            s[0].toInt() .. s[1].toInt()
        }
    }

    override fun part1() {
        val result = ranges.count { set ->
            // Add one to the count if ALL elements in one set are present in the other.
            set[0].all { set[1].contains(it) } || set[1].all { set[0].contains(it) }
        }
        println("Part 1 result: $result")
    }

    override fun part2() {
        val result = ranges.count { set ->
            // Add one to the count if ANY elements in one set are present in the other.
            set[0].any { set[1].contains(it) } || set[1].any { set[0].contains(it) }
        }
        println("Part 2 result: $result")
    }
}