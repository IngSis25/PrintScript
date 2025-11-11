package org.example.output

/**
 * Output implementation for testing that captures all writes
 */
class TestOutput : Output {
    val printsList = mutableListOf<String>()

    override fun write(msg: String) {
        printsList.add(msg)
    }
}
