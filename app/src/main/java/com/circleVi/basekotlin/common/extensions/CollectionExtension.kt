package com.circleVi.basekotlin.common.extensions

fun <T> MutableList<T>.removeAt(startIndex: Int, count: Int) {
    for (i in startIndex until startIndex + count) {
        if (startIndex >= this.size) {
            return
        }
        this.removeAt(startIndex)
    }
}

inline fun <T> List<T>.forEachDown(action: (T) -> Unit) {
    val iterator = this.listIterator(size)
    while (iterator.hasPrevious()) {
        action.invoke(iterator.previous())
    }
}

inline fun <T> List<T>.forEachIndexedDown(action: (index: Int, T) -> Unit) {
    val iterator = this.listIterator(size)
    var index = size - 1
    while (iterator.hasPrevious()) {
        action.invoke(index--, iterator.previous())
    }

    this.forEach {  }
}

inline fun <T, R> MutableList<R>.addAll(list: List<T>, block: (T) -> R) {
    list.forEach {
        this.add(block(it))
    }
}

inline fun <T> Iterable<T>.any(predicate: (index: Int, T) -> Boolean): Boolean {
    if (this is Collection && isEmpty()) return false
    this.forEachIndexed { index, element ->
        if (predicate(index, element)) return true
    }
    return false
}

inline fun <T> MutableList<T>.removeBy(predicate: (T) -> Boolean) {
    val listIterator = listIterator(size)
    while (listIterator.hasPrevious()) {
        val element = listIterator.previous()
        if (predicate(element)) {
            remove(element)
        }
    }
}

inline fun <E> MutableList<E>.removeItemIf(predicate: (e: E) -> Boolean) {
    for (i in this.indices) {
        if (predicate.invoke(this[i])) {
            this.removeAt(i)
            return
        }
    }
}