package parser.rules

import builders.NodeBuilder
import main.kotlin.lexer.LiteralNumber
import main.kotlin.lexer.OperatorType
import matchers.SequenceMatcher
import matchers.TokenMatcher

class ExpressionRule(
    override val builder: NodeBuilder,
) : ParserRule {
    override val matcher =
        SequenceMatcher(
            listOf(
                TokenMatcher(LiteralNumber),
                TokenMatcher(OperatorType),
                TokenMatcher(LiteralNumber),
            ),
        )
}
