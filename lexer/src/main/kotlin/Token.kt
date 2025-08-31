package main.kotlin.lexer

import org.example.TokenType

data class Token(
    val type: TokenType,
    val value: String,
    val line: Int,
    val column: Int,
)
