package parser

import main.kotlin.lexer.Token
import main.kotlin.lexer.TokenProvider
import main.kotlin.parser.ParseResult
import main.kotlin.parser.Parser

class DefaultParser(private val tokenProvider: TokenProvider) : Parser {
    override fun parse(tokens: List<Token>): ParseResult {
        TODO("Not yet implemented")
    }
}