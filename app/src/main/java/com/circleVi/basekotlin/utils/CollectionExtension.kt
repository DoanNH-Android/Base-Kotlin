package com.circleVi.basekotlin.utils

import java.util.*


fun <T> ArrayList<T>.removeBy(predicate: (T) -> Boolean) {
    val listIterator = listIterator(size)
    while (listIterator.hasPrevious()) {
        val element = listIterator.previous()
        if (predicate(element)) {
            remove(element)
        }
    }
}