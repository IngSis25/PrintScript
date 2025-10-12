import builders.NodeBuilder
import matchers.TokenMatcher
import parser.matchers.Matcher
import rules.BooleanExpressionRule
import types.LiteralBoolean

class LiteralBooleanRule(
    override val builder: NodeBuilder,
) : BooleanExpressionRule {
    override val matcher: Matcher<*> = TokenMatcher(LiteralBoolean)
}
