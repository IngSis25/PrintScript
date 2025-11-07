package org.example.Lexer

class Token(
    val type: String,
    val value: String,
    val location: Location,
) {
    override fun toString(): String = "Token($type($type($value) in $location)"
}
