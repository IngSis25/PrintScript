package main.kotlin.lexer

// para representar una regla (regex + tipo + ignore).
data class TokenRule(
    val pattern: Regex,
    val ignore: Boolean = false,
)
