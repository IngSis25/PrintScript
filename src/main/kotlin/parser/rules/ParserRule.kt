package parser.rules

import main.kotlin.lexer.Token
import parser.matchers.Matcher

interface ParserRule {
    val matcher: Matcher<List<Token>>

}
