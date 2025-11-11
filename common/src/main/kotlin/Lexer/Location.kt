package org.example.Lexer

class Location(
    private val line: Int,
    private val column: Int,
) {
    fun getLine(): Int = line

    fun getColumn(): Int = column

    override fun toString(): String = "(line: $line, column: $column)"
}
