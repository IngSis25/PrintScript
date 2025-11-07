package rules

import builders.NodeBuilder
import matchers.SequenceMatcher
import matchers.TokenMatcher
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
