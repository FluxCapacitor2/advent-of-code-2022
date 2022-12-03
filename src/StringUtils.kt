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
