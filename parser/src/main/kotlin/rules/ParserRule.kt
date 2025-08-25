package parser.rules

import ASTNode
import main.kotlin.lexer.Token
import parser.matchers.Matcher

interface ParserRule {
    // RuleMatcher No usa el tipo de nodo, solo se fija si matchea o no, por eso no le paso un tipo especifico
    val matcher: Matcher<*>

    // val matcher: Matcher<List<Token>>
    fun buildNode(matchedTokens: List<Token>): ASTNode // cada regla sabe construir su propio nodo
}
