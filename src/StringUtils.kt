import java.util.function.Predicate

/**
 * Splits the [Iterable] whenever the [predicate] returns true
 * on one of the items in the Iterable. If the predicate returns
 * true for an item, it is *not* included in the final list.
 *
 * Example: `[1, 2, 3, 4, 5, 6, 7, 8, 9, 10].splitOn { it % 3 == 0}`
 * results in `[[1, 2], [4, 5], [7, 8], [10]]`.
 */
fun <T> Iterable<T>.splitOn(predicate: Predicate<T>): MutableList<List<T>> {
    val list = toList()

    val temp = mutableListOf<T>()
    val done = mutableListOf<List<T>>()

    list.forEach { item ->
        if (predicate.test(item)) {
            done.add(temp.toList())
            temp.clear()
        } else {
            temp.add(item)
        }
    }

    if (temp.isNotEmpty())
        done.add(temp.toList())

    return done
}
