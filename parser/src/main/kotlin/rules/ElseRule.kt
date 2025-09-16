package rules

import builders.NodeBuilder
import main.kotlin.lexer.Token
import matchers.BlockMatcher
import matchers.SequenceMatcher
import matchers.TokenMatcher
import parser.matchers.Matcher
import parser.rules.ParserRule
import types.ElseType

class ElseRule(
    override val builder: NodeBuilder,
) : ParserRule {
    override val matcher: Matcher<*> =
        SequenceMatcher(
            listOf(
                TokenMatcher(ElseType),
                BlockMatcher(),
            ) as List<Matcher<Token>>,
        )
}
