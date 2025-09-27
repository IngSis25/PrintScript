package parser.rules

import builders.NodeBuilder
import parser.matchers.Matcher

interface ParserRule {
    val builder: NodeBuilder

    // RuleMatcher No usa el tipo de nodo, solo se fija si matchea o no, por eso no le paso un tipo especifico
    val matcher: Matcher<*>

    // val matcher: Matcher<List<Token>>
    // fun buildNode(matchedTokens: List<Token>): ASTNode // cada regla sabe construir su propio nodo -> NodeBuilder
}
