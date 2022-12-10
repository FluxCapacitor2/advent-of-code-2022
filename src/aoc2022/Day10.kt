package aoc2022

import Day

object Day10 : Day(2022, 10) {

    override fun part1() {

        fun shouldRecord(num: Int) = listOf(20, 60, 100, 140, 180, 220).contains(num)

        var cycles = 0
        var register = 1
        val values = mutableListOf<Int>()
        for (inst in lines) {
            val split = inst.split(' ')
            cycles++

            if (shouldRecord(cycles)) {
                values.add(cycles * register)
            }

            when (split[0]) {
                "noop" -> {}

                "addx" -> {
                    cycles++ // The `addx` instruction takes two cycles to complete
                    if (shouldRecord(cycles)) { // Make sure the value is recorded upon completion, before the register is updated
                        values.add(cycles * register)
                    }
                    register += split[1].toInt()
                }
            }
        }

        val result = values.sum()
        println("Part 1 result: $result")
    }

    override fun part2() {
        var cycles = 0
        var register = 1
        println("Part 2 result:")
        var result = ""
        for (inst in lines) {
            val split = inst.split(' ')
            cycles++
            var isLit = ((cycles - 1) % 40) - register in -1..1
            if (split[0] == "addx") {
                result += if (isLit) "#" else "."
                if (cycles % 40 == 0) result += "\n"
                isLit = (cycles % 40) - register in -1..1
                cycles++ // The `addx` instruction takes two cycles to complete
                register += split[1].toInt()
            }
            result += if (isLit) "#" else "."
            if (cycles % 40 == 0) result += " [$cycles] \n"
        }
        println(result.dropLast(1))
    }
}