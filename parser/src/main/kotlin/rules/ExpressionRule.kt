package parser.rules

import main.kotlin.lexer.LiteralNumber
import main.kotlin.lexer.OperatorType
import parser.matchers.SequenceMatcher
import parser.matchers.TokenMatcher

class ExpressionRule: ParserRule {
    val matcher = SequenceMatcher(
        listOf(
            TokenMatcher(LiteralNumber),
            TokenMatcher(OperatorType),
            TokenMatcher(LiteralNumber),
        )
    )
}