package org.example.iterator

interface PrintScriptIterator<T> {
    fun hasNext(): Boolean

    fun next(): T?

    fun peek(): T?
}
