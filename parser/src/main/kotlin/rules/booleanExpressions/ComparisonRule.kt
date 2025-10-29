import builders.NodeBuilder
import matchers.MultiTypeTokenMatcher
import matchers.SequenceMatcher
import matchers.TokenMatcher
import parser.matchers.Matcher
import rules.BooleanExpressionRule
import types.ComparisonOperatorType
import types.IdentifierType

class ComparisonRule(
    override val builder: NodeBuilder,
) : BooleanExpressionRule {
    override val matcher: Matcher<*> =
        SequenceMatcher(
            listOf(
                TokenMatcher(IdentifierType), // left: identifier
                TokenMatcher(ComparisonOperatorType), // operator: ==, !=, >, <, >=, <=
                MultiTypeTokenMatcher(
                    listOf( // right: identifier o literal number
                        IdentifierType,
                        LiteralNumber,
                        LiteralString,
                    ),
                ),
            ),
        )
}
