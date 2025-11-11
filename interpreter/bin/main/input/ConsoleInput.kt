package org.example.input

class ConsoleInput : Input {
    override fun read(message: String): String = readlnOrNull() ?: ""
}
