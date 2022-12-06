package aoc2022

import Day
import columns
import splitOn

object Day05 : Day(2022, 5) {

    private val cratesText = lines.splitOn { it.isBlank() }[0].dropLast(1)
    private val crates = cratesText.map {
        it.chunked(4).map { c ->
            Crate.create(c)
        }
    }

    private val instructions = lines.splitOn { it.isBlank() }[1].map {
        Instruction.create(it)
    }

    // The top of each column is the 0th index
    // Switch the dimensions of the array and remove empty crates
    private val columns = crates.columns().map { column ->
        column.filter { crate ->
            crate.filled
        }
    }

    data class Crate(var filled: Boolean, var letter: String? = null) {
        companion object {
            fun create(input: String): Crate {
                if (input.isBlank()) return Crate(false)
                val letter = input.trim().substringAfter('[').substringBefore(']')
                return Crate(true, letter)
            }
        }
    }

    data class Instruction(val amount: Int, val from: Int, val to: Int) {
        companion object {
            fun create(input: String): Instruction {
                val split = input.split(' ')
                return Instruction(split[1].toInt(), split[3].toInt(), split[5].toInt())
            }
        }
    }

    override fun part1() {
        val columns = ArrayList(columns.map { ArrayList(it) }) // Make a deep copy of the input
        instructions.forEach {
            for (i in 0 until it.amount) {
                // Remove a crate from the top of the stack
                val crate = columns[it.from - 1].removeAt(0)
                // Add it to the destination stack
                columns[it.to - 1].add(0, crate)
            }
        }
        // Print out the top letter of each stack of crates
        println("Part 1 result: ${columns.joinToString(separator = "") { it.first().letter!! }}")
    }

    override fun part2() {
        val columns = ArrayList(columns.map { ArrayList(it) }) // Make a deep copy of the input
        instructions.forEach {
            // Get a list of crates from the top of the stack
            val crates = columns[it.from - 1].subList(0, it.amount)
            // Add them to the destination stack
            columns[it.to - 1].addAll(0, crates)
            // Clear them from the previous stack by using `clear()` on a mutable view of the list
            crates.clear()
        }
        // Print out the top letter of each stack of crates
        println("Part 2 result: ${columns.joinToString(separator = "") { it.first().letter!! }}")
    }
}