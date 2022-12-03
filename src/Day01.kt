/**
 * https://adventofcode.com/2022/day/1
 * Input: https://adventofcode.com/2022/day/1/input
 */
fun main() {

    /*************** Setup ***************/

    // Read input from a file
    val input = readInput("input/day01.txt")
    // Split the input based on blank lines between sets of numbers
    val temp = mutableListOf<Int>()
    val sets = mutableListOf<List<Int>>()
    input
        .map { it.toIntOrNull() } // Convert the inputs to integers, or null if they couldn't be converted (i.e. a blank line)
        .map {
            if (it == null) {
                sets.add(temp.toList()) // Using `toList` copies the list, preventing further modification from changing the value added to `sets`.
                temp.clear()
            } else {
                temp += it
            }
        }

    /*************** Part 1 ***************/

    // Sum each set of numbers and find the greatest
    val greatest = sets.maxOf { it.sum() }
    // Print the result
    println("Part 1 result: $greatest")

    /*************** Part 2 ***************/

    val greatestThree = sets
        .sortedByDescending { it.sum() } // Sort the sets by their sum
        .take(3) // Take the top three
        .sumOf { it.sum() } // Calculate the total amount of calories
    println("Part 2 result: $greatestThree")
}
