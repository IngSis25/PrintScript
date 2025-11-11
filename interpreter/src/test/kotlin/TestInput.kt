package org.example.input

import java.util.LinkedList
import java.util.Queue

/**
 * Input implementation for testing that reads from a queue of strings
 */
class TestInput : Input {
    private val inputQueue: Queue<String> = LinkedList()

    fun addInput(value: String) {
        inputQueue.add(value)
    }

    fun addInputs(vararg values: String) {
        values.forEach { inputQueue.add(it) }
    }

    override fun read(message: String): String = inputQueue.poll() ?: ""

    fun clear() {
        inputQueue.clear()
    }
}
