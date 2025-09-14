package rules

import TokenMatcher
import builders.NodeBuilder
import matchers.SequenceMatcher
import org.example.LiteralNumber
import parser.rules.ParserRule
import types.OperatorType

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
