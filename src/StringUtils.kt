fun String.mostCommonChar(): Char {
    return toList().distinct().maxBy {
        count { c -> c == it }
    }
}

fun String.leastCommonChar(): Char {
    return toList().distinct().minBy {
        count { c -> c == it }
    }
}

infix fun String.intersect(other: String): Set<Char> {
    return toSet() intersect other.toSet()
}

// Allows for repeated chaining of [intersect] with multiple strings
infix fun Set<Char>.intersect(other: String) = intersect(other.toSet())

fun Iterable<Char>.concatToString() = toList().toCharArray().concatToString()