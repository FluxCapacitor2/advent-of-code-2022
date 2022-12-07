package aoc2022

import Day

object Day07 : Day(2022, 7) {

    data class Node(val name: String, val size: Int) {
        var parent: Node? = null
        private val children = mutableListOf<Node>()

        fun addChild(name: String, size: Int) {
            Node(name, size).let {
                children.add(it)
                it.parent = this
            }
        }

        // Get the total size of this node and all of its children, recursively
        fun totalSize(): Int = allChildren().sumOf { it.size }

        // Find directories (zero size) containing children with a size that add up to be less than 100,000
        fun matches(): List<Node> = allChildren().filter { it.size == 0 && it.totalSize() <= 100_000 }

        fun findOrCreate(name: String, size: Int): Node {
            children.find { it.name == name && it.size == size }?.let {
                return it
            }
            // If the node does not already exist, create it and add it as a child
            Node(name, size).let {
                children.add(it)
                it.parent = this
                return it
            }
        }

        override fun toString(): String {
            return toString(0)
        }

        private fun toString(depth: Int): String {
            if (children.isEmpty()) return "\t".repeat(depth) + name + " (" + size + ")"
            return "\t".repeat(depth) + "$name: ${totalSize()} total\n" + children.joinToString("\n") {
                it.toString(depth + 1)
            }
        }

        fun allChildren(): List<Node> {
            return children.flatMap { it.allChildren() } + this
        }
    }

    private val tree: Node

    init {
        // Prepare the input
        var currentNode = Node("/", 0)

        for (line in lines) {
            if (line.startsWith("$ ")) {

                val fullCommand = line.substring(2)
                when (val cmd = fullCommand.substringBefore(' ')) {
                    "cd" -> {
                        val path = fullCommand.substringAfter(' ')
                        if (path == "..") {
                            // Go up 1 directory
                            currentNode = currentNode.parent!!
                        } else if (path == "/") {
                            // Go up to the root of the tree
                            while (currentNode.parent != null) currentNode = currentNode.parent!!
                        } else {
                            currentNode = currentNode.findOrCreate(path, 0)
                        }
                    }

                    "ls" -> continue
                    else -> error("Unknown command: $cmd")
                }
            } else if (line.startsWith("dir ")) {
                currentNode.findOrCreate(line.substringAfter("dir "), 0)
            } else {
                val split = line.split(" ")
                val size = split.first().toInt()
                val name = split.last()
                currentNode.addChild(name, size)
            }
        }
        // Go all the way back up to the root of the tree
        while (currentNode.parent != null)
            currentNode = currentNode.parent!!

        tree = currentNode
    }

    override fun part1() {
        val matches = tree.matches()
        val result = matches.sumOf { it.totalSize() }
        println("Part 1 result: $result")
    }

    override fun part2() {
        val total = 70_000_000
        val minimum = 30_000_000
        val used = tree.totalSize()
        val free = total - used

        val mustFree = minimum - free

        val smallest = tree.allChildren().filter {
            it.size == 0 && it.totalSize() > mustFree
        }.minBy {
            it.totalSize() - mustFree
        }
        val result = smallest.totalSize()
        println("Part 2 result: $result")
    }
}