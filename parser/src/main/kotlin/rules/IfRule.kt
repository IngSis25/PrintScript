package rules

import IfType
import TokenMatcher
import builders.NodeBuilder
import main.kotlin.lexer.Token
import matchers.BlockMatcher
import matchers.SequenceMatcher
import matchers.OptionalMatcher
import parser.matchers.Matcher
import parser.rules.ParserRule
import types.ElseType
import types.IdentifierType
import types.PunctuationType

class IfRule(
    override val builder: NodeBuilder
) : ParserRule {
    override val matcher: Matcher<*> =
        SequenceMatcher(
            listOf(
                TokenMatcher(IfType),
                TokenMatcher(PunctuationType, "("),
                TokenMatcher(IdentifierType),
                TokenMatcher(PunctuationType, ")"),
                BlockMatcher(),
                OptionalMatcher(SequenceMatcher( //si no hay else el sequence matcher falla pero el optional no lo considera error
                    listOf(
                        TokenMatcher(ElseType),
                        BlockMatcher()
                    ) as List<Matcher<Token>>
                ))
            ) as List<Matcher<Token>>
        )
}
