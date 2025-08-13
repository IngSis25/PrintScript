package parser.rules

import main.kotlin.lexer.Token
import main.kotlin.parser.ASTNode
import parser.matchers.Matcher

interface ParserRule{
    val matcher: Matcher<List<Token>>
    fun buildNode(matchedTokens: List<Token>): ASTNode //cada regla sabe construir su propio nodo
}
