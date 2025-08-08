package main.kotlin.lexer

//para representar una regla (regex + tipo + ignore).
data class TokenRule (val pattern: Regex,
                      val type: TokenType,
                      val ignore: Boolean = false )