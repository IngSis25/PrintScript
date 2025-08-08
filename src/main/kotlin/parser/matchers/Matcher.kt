package parser.matchers

import main.kotlin.lexer.Token
import main.kotlin.parser.ParseResult

interface Matcher {
    fun match(tokens: List<Token>, pos: Int) : ParseResult?
}