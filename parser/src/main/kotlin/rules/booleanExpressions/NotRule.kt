package parser.rules

import TokenMatcher
import builders.NodeBuilder
import main.kotlin.lexer.Token
import matchers.SequenceMatcher
import parser.matchers.Matcher
import types.PunctuationType

class NotRule(
    private val innerRule: BooleanExpressionRule,
    override val builder: NodeBuilder,
) : BooleanExpressionRule {
    override val matcher: Matcher<*> =
        SequenceMatcher(
            listOf(
                TokenMatcher(PunctuationType, "!"),
                innerRule.matcher,
            ) as List<Matcher<Token>>,
        )
}
