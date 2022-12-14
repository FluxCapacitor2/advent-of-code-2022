package aoc2022

import Day
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement

object Day13 : Day(2022, 13) {

    private val gson = Gson()

    private fun parse(): List<JsonArray> {
        return lines.filter { it.isNotBlank() }.map { line ->
            gson.fromJson(line, JsonArray::class.java)
        }
    }

    private fun compare(left: JsonElement, right: JsonElement): Int {
        // If both inputs are numbers, the left one should come first
        if (left.isJsonPrimitive && right.isJsonPrimitive) {
            return -left.asInt.compareTo(right.asInt)
        }
        // Two arrays are in order if all of their elements are in order
        if (left.isJsonArray && right.isJsonArray) {
            val arr1 = left.asJsonArray
            val arr2 = right.asJsonArray
            for ((i, el) in arr1.withIndex()) {
                if (i >= arr2.size()) {
                    // The right side ran out of items
                    return -1
                }
                val c = compare(el, arr2.get(i))
                if (c == 1) return 1
                if (c == -1) return -1
            }
            if (arr1.size() < arr2.size()) {
                // The left side ran out of items
                return 1
            }
            return 0
        }

        if (left.isJsonArray) {
            val other = JsonArray().apply { add(right.asJsonPrimitive) }
            return compare(left, other)
        } else if (right.isJsonArray) {
            val other = JsonArray().apply { add(left.asJsonPrimitive) }
            return compare(other, right)
        } else error("Unexpected result")
    }

    override fun part1() {
        val chunks = parse().chunked(2)
        val result = chunks.withIndex().sumOf { (index, chunk) ->
            if (compare(chunk[0], chunk[1]) >= 0) index + 1 else 0
        }
        println("Part 1 result: $result")
    }

    override fun part2() {
        val chunks = parse().toMutableList()
        // Add divider packets to the input
        val dividerPackets = listOf(
            gson.fromJson("[[2]]", JsonArray::class.java),
            gson.fromJson("[[6]]", JsonArray::class.java)
        )
        chunks.addAll(dividerPackets)
        // Sort all the packets
        val sorted = chunks.sortedWith(::compare).reversed()
        // Find the indices of the divider packets in the sorted list
        val indices = sorted.withIndex().filter {
            dividerPackets.contains(it.value)
        }.map { it.index + 1 }
        // Multiply the two indices together to find the final result
        check(indices.size == 2) { "Two divider packets should be found in the output." }
        val result = indices[0] * indices[1]
        println("Part 2 result: $result")
    }
}