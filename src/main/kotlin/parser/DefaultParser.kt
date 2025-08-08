package main.kotlin.parser

import main.kotlin.lexer.Token
import main.kotlin.lexer.TokenProvider

class DefaultParser(private val tokenProvider: TokenProvider) : Parser {
    override fun parse(tokens: List<Token>): ParseResult {
        TODO("Not yet implemented")
    }
}