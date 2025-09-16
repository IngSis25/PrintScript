import TokenMatcher
import builders.NodeBuilder
import parser.matchers.Matcher
import parser.rules.BooleanExpressionRule
import types.IdentifierType

class BooleanIdentifierRule(
    override val builder: NodeBuilder,
) : BooleanExpressionRule {
    override val matcher: Matcher<*> = TokenMatcher(IdentifierType)
}
