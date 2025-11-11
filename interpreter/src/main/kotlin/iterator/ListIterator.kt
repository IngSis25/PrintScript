package org.example.iterator

/**
 * Simple iterator that wraps a list of items
 */
class ListIterator<T>(
    private val items: List<T>,
) : PrintScriptIterator<T> {
    private var index = 0
    private var peekedElement: T? = null

    override fun hasNext(): Boolean = peekedElement != null || index < items.size

    override fun next(): T? {
        if (peekedElement != null) {
            val temp = peekedElement
            peekedElement = null
            return temp
        }
        return if (index < items.size) {
            items[index++]
        } else {
            null
        }
    }

    override fun peek(): T? {
        if (peekedElement != null) {
            return peekedElement
        }
        if (index < items.size) {
            peekedElement = items[index]
            return peekedElement
        }
        return null
    }
}
