package aoc2022

import Day
import combinedSize
import kotlinx.coroutines.*
import union
import kotlin.math.abs

object Day15 : Day(2022, 15) {
    override fun part1() {
        val row = 2000000
        val sensors = getSensors()

        val ranges = sensors.mapNotNull {
            it.exclusion.getInRow(row)
        }

        val result = ranges.combinedSize()
        println("Part 1 result: $result")
    }

    override fun part2() {
        val sensors = getSensors()
        val deferred = CompletableDeferred<Long>()

        // Start multiple async tasks which race to find the solution
        val stepSize = 4_000_000 / Runtime.getRuntime().availableProcessors()
        (0 until 4_000_000 step stepSize).map { start ->
            CoroutineScope(Dispatchers.Default).async {
                scanRows(sensors, start..start + stepSize, deferred)
            }
        }

        // When the first one completes, its value is returned to the CompletableDeferred
        // and the "await" call stops blocking.
        val result = runBlocking { deferred.await() }
        println("Part 2 result: $result")
    }

    private fun scanRows(sensors: List<Sensor>, rows: IntRange, deferred: CompletableDeferred<Long>) {
        for (row in rows) {
            val coveredRanges = sensors.mapNotNull { s ->
                s.exclusion.getInRow(row)
            }.union()
            if (coveredRanges.size > 1) {
                // There is a gap in the sensors' covered ranges. The beacon could be here.
                check(coveredRanges.size == 2) { "There should be one empty space in between two ranges: $coveredRanges" }
                val lower = coveredRanges.first().last
                val upper = coveredRanges.last().first
                check(upper - lower == 2) { "There should be a space of one unit between ranges: $upper - $lower = ${upper - lower}" }
                val x = upper - 1
                // Use multiplyExact and addExact to avoid potential long overflow
                val result = Math.addExact(Math.multiplyExact(x.toLong(), 4_000_000L), row.toLong())
                deferred.complete(result)
                return
            }
        }
    }

    private fun getSensors() = lines
        .filter { it.isNotBlank() }
        .map { line -> Sensor(line) }

    data class Exclusion(val sensorX: Int, val sensorY: Int, val distance: Int) {
        fun getInRow(row: Int): IntRange? {
            val e = distance - abs(sensorY - row)
            val range = (sensorX - e)..(sensorX + e)
            return if (range.first < range.last) range else null
        }

        fun contains(x: Int, y: Int): Boolean = abs(x - this.sensorX) + abs(y - this.sensorY) <= distance
    }

    class Sensor(line: String) {
        private val sensorX: Int
        private val sensorY: Int
        private val beaconX: Int
        private val beaconY: Int

        companion object {
            private val regex =
                "Sensor at x=([\\d-]+), y=([\\d-]+): closest beacon is at x=([\\d-]+), y=([\\d-]+)".toRegex()
        }

        init {
            val (x, y, beaconX, beaconY) = regex.matchEntire(line)!!.destructured
            this.sensorX = x.toInt()
            this.sensorY = y.toInt()
            this.beaconX = beaconX.toInt()
            this.beaconY = beaconY.toInt()
        }

        val exclusion by lazy {
            // Create a representation of the positions that a beacon could NOT be located,
            // based on the distance to the nearest beacon.
            val distance = abs(sensorX - beaconX) + abs(sensorY - beaconY)
            Exclusion(sensorX, sensorY, distance)
        }

        override fun toString(): String {
            return "Sensor(x=$sensorX, y=$sensorY, beaconX=$beaconX, beaconY=$beaconY)"
        }
    }
}
