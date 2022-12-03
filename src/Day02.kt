/**
 * https://adventofcode.com/2022/day/2
 *
 * Input: https://adventofcode.com/2022/day/2/input
 */
fun main() {

    /*************** Setup ***************/

    // Read input from a file
    val input = readInput("input/day02.txt")
        // Split each line into a player and opponent move; they are separated by one space
        .map { it.split(' ') }

    /*************** Part 1 ***************/

    val totalPointValue1 = input
        .map { it.map(RPS::from) } // Convert the letters into either Rock, Paper, or Scissors
        .sumOf { (opponentMove, playerMove) ->
            // Get the point value of the turn.
            val outcomeValue = playerMove.against(opponentMove).pointValue // Winning gives 6 points, drawing gives 3 points, and losing rewards no points.
            val shapeValue = playerMove.shapeValue // Rock = 1 point, Paper = 2 points, Scissors = 3 points
            return@sumOf outcomeValue + shapeValue // The score for each round is the sum of the sub-scores.
        }

    // Print the result
    println("Part 1 result: $totalPointValue1")

    /*************** Part 2 ***************/

    val totalPointValue2 = input
        .map { RPS.from(it[0]) to Outcome.from(it[1]) } // Convert the strings into a nicer format
        .sumOf { (opponentMove, outcome) ->
            // The player's move must be computed based on the fixed outcome.
            // An outcome of X means a loss, Y means a draw, and Z means a win.
            val playerMove = RPS.values().find { it.against(opponentMove) == outcome }!!
            val shapeValue = playerMove.shapeValue
            val outcomeValue = outcome.pointValue
            return@sumOf shapeValue + outcomeValue
        }

    println("Part 2 result: $totalPointValue2")

}

/**
 * An enum that represents a shape (either Rock, Paper, or Scissors).
 */
enum class RPS(val shapeValue: Int) {
    ROCK(1), PAPER(2), SCISSORS(3);

    /**
     * Plays this shape against the other shape, returning the outcome.
     * For example: RPS.PAPER.against(RPS.ROCK) results in Outcome.WIN.
     */
    fun against(other: RPS): Outcome = when (this) {
        ROCK -> when (other) {
            ROCK -> Outcome.DRAW
            PAPER -> Outcome.LOSS
            SCISSORS -> Outcome.WIN
        }

        PAPER -> when (other) {
            ROCK -> Outcome.WIN
            PAPER -> Outcome.DRAW
            SCISSORS -> Outcome.LOSS
        }

        SCISSORS -> when (other) {
            ROCK -> Outcome.LOSS
            PAPER -> Outcome.WIN
            SCISSORS -> Outcome.DRAW
        }
    }

    companion object {
        /**
         * Converts a string (A/B/C or X/Y/Z) into a [RPS] object.
         */
        fun from(string: String) = when (string) {
            "A", "X" -> ROCK
            "B", "Y" -> PAPER
            "C", "Z" -> SCISSORS
            else -> error("Invalid string: $string")
        }
    }
}

enum class Outcome(val pointValue: Int) {
    WIN(6), LOSS(0), DRAW(3);

    companion object {
        fun from(outcomeString: String) = when (outcomeString) {
            "X" -> LOSS
            "Y" -> DRAW
            "Z" -> WIN
            else -> error("Invalid outcome string: $outcomeString")
        }
    }
}
