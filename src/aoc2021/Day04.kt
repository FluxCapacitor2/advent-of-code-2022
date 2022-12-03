package aoc2021

import columns
import fetch
import splitOn
import takeAllAfter

fun main() {
    Day04.part1()
    Day04.part2()
}

object Day04 {

    private val input = fetch(2021, 4).lines()

    private val order = input.first().split(',').map { it.toInt() }
    private val parsedBoards = input.takeAllAfter(2).splitOn { it.isBlank() }.mapIndexed { i, it ->
        val squares = it.map { line ->
            line.split(" ").mapNotNull { number ->
                number.toIntOrNull()
            }
        }
        BingoBoard(i + 1, squares)
    }

    private fun getFreshList() = parsedBoards.map { it.reset() }

    fun part1() {
        val boards = getFreshList()
        for (number in order) {
            boards.forEach { it.draw(number) }
            val winner = boards.find { it.hasWin() }
            if (winner != null) {
                val finalScore = winner.unmarkedSquareScore() * number
                println("Part 1 result: $finalScore (${winner.unmarkedSquareScore()} * $number)")
                winner.print()
                break
            }
        }
    }

    fun part2() {
        val boards = getFreshList().toMutableList()
        for (number in order) {
            for (board in ArrayList(boards)) { // Copy the list to avoid a ConcurrentModificationException
                board.draw(number)
                if (board.hasWin()) {
                    boards.remove(board) // We don't care if the board wins unless it's the last one
                    if (boards.isEmpty()) {
                        val finalScore = board.unmarkedSquareScore() * number
                        println("Part 2 result: $finalScore (${board.unmarkedSquareScore()} * $number)")
                        board.print()
                        break
                    }
                }
            }
        }
    }

    private data class BingoBoard(val id: Int, val squares: List<List<Int>>) {

        /**
         * A list with the same dimensions as [squares],
         * representing the squares on the board which have been drawn.
         */
        private val drawn = squares.indices.map {
            mutableListOf(
                *squares[it].indices.map { false }.toTypedArray()
            )
        }

        fun draw(number: Int) {
            squares.forEachIndexed { rowN, row ->
                row.forEachIndexed { colN, n ->
                    if (n == number) drawn[rowN][colN] = true
                }
            }
        }

        fun hasWin(): Boolean {
            for (row in squares.indices) {
                if (drawn[row].all { it } || drawn.columns()[row].all { it }) {
                    return true
                }
            }
            return false
        }

        fun unmarkedSquareScore(): Int {
            var score = 0
            for ((rowN, row) in squares.withIndex()) {
                for ((colN, number) in row.withIndex()) {
                    if (!drawn[rowN][colN]) score += number
                }
            }
            return score
        }

        fun print() {
            for ((rowN, row) in squares.withIndex()) {
                for ((colN, col) in row.withIndex()) {
                    if (drawn[rowN][colN]) {
                        print(col.toString().padStart(2, ' ') + " ")
                    } else print("__ ")
                }
                println()
            }
        }

        fun reset() = BingoBoard(id, squares)
    }
}