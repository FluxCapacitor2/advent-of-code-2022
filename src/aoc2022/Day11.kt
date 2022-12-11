package aoc2022

import Day
import splitOn

object Day11 : Day(2022, 11) {

    private val monkeys = mutableListOf<Monkey>()

    data class Monkey(
        val items: MutableList<Long>,
        val operation: (Long) -> Long,
        val test: (Long) -> Boolean,
        val ifTrue: Int,
        val ifFalse: Int,
        var inspected: Int = 0
    ) {
        fun process(divideByThree: Boolean = true) {
            items.removeAll { item ->
                inspected++
                val newLevel = if (divideByThree) {
                    operation(item) / 3
                } else {
                    operation(item)
                }
                val result = test(newLevel)
                if (result) {
                    monkeys[ifTrue].items.add(newLevel)
                } else {
                    monkeys[ifFalse].items.add(newLevel)
                }
                return@removeAll true
            }
        }
    }

    fun readInput(useModuloTrick: Boolean) {
        monkeys.clear()
        // This number keeps track of the product of all divisors that are tested
        // (see later comment)
        var totalModulus = 1
        for (lineGroup in lines.splitOn { it.isEmpty() }) {
            val group = lineGroup.map { it.trim() }

            val eq = group[2].substringAfter("new = ").split(' ') // The full equation, e.g. ["old", "*", "5"]
            val factor = group[3].substringAfter("divisible by ")
                .toInt() // The required divisor for the condition to return true

            val monkey = Monkey(
                items = group[1].substringAfter("Starting items: ")
                    .split(", ") // Split the string by comma into a list of substrings
                    .map { it.toLong() }.toMutableList(), // Convert the strings to numbers
                operation = { num ->

                    val input1 = eq[0].toLongOrNull() ?: num
                    val input2 = eq[2].toLongOrNull() ?: num

                    val newValue = when (val op = eq[1]) {
                        // addExact and multiplyExact throw exceptions when numbers overflow.
                        "+" -> Math.addExact(input1, input2)
                        "*" -> Math.multiplyExact(input1, input2)
                        else -> error("Invalid operation: $op")
                    }
                    // The value returned after the operation must be reduced to a smaller number
                    // for the program to run in a reasonable amount of time. Because we are only using
                    // the worry-values to check if they are divisible, applying a modulus operation
                    // with the product of all divisors that we check will keep the numbers at a relatively
                    // small size (under 2^63 - 1) while preserving the results of divisibility checks.
                    if (useModuloTrick) newValue % totalModulus
                    // Note: This trick does not apply when the worry-values are divided by 3 after every
                    // inspection, because that alters divisibility of the numbers.
                    else newValue
                },
                test = { num ->
                    num % factor == 0L
                },
                ifTrue = group[4].substringAfter("throw to monkey ").toInt(),
                ifFalse = group[5].substringAfter("throw to monkey ").toInt()
            )
            monkeys.add(monkey)
            totalModulus *= factor
        }
    }

    override fun part1() {
        readInput(useModuloTrick = false)
        // Process 20 rounds, where worry levels are divided by three every time they're processed.
        repeat(20) {
            monkeys.forEach {
                it.process(divideByThree = true)
            }
        }

        println("Part 1 result: ${getMonkeyBusinessLevel()}")

    }

    override fun part2() {
        readInput(useModuloTrick = true)
        // Process 10,000 rounds, where worry levels are not divided by three every time.
        // A modulus trick is needed to keep the numbers at a reasonable (< Long.MAX_VALUE) size.
        repeat(10000) {
            monkeys.forEach {
                it.process(divideByThree = false)
            }
        }

        println("Part 2 result: ${getMonkeyBusinessLevel()}")
    }

    /**
     * Finds the top two monkeys based on their amount of items
     * inspected, and returns the product of these two numbers.
     */
    private fun getMonkeyBusinessLevel(): Long {
        return monkeys
            .sortedByDescending { it.inspected } // Sort the monkeys by their amounts of items inspected (with the highest first)
            .take(2) // Take the top two
            .map { it.inspected.toLong() } // Multiply them together
            .reduce { acc, i -> acc * i }
    }
}