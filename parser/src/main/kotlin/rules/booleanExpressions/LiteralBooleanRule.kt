import builders.NodeBuilder
import parser.matchers.Matcher
import parser.rules.BooleanExpressionRule
import types.LiteralBoolean

class LiteralBooleanRule(
    override val builder: NodeBuilder,
) : BooleanExpressionRule {
    override val matcher: Matcher<*> = TokenMatcher(LiteralBoolean)
}
