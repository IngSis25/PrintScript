package org.example.input

interface Input {
    /**
     * Reads a message from the user.
     */
    infix fun read(message: String): String
}
