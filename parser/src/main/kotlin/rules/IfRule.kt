package rules

import builders.NodeBuilder
import main.kotlin.lexer.Token
import matchers.*
import parser.matchers.Matcher
import parser.rules.ParserRule
import types.ElseType
import types.IdentifierType
import types.IfType
import types.LiteralBoolean
import types.PunctuationType

class IfRule(
    override val builder: NodeBuilder,
) : ParserRule {
    override val matcher: Matcher<*> =
        SequenceMatcher(
            listOf(
                TokenMatcher(IfType),
                TokenMatcher(PunctuationType, "("),
                MultiTypeTokenMatcher(listOf(IdentifierType, LiteralBoolean)), // Acepta identificadores y literales
                TokenMatcher(PunctuationType, ")"),
                BlockMatcher(),
                OptionalMatcher(
                    SequenceMatcher( // si no hay else el sequence matcher falla pero el optional no lo considera error
                        listOf(
                            TokenMatcher(ElseType),
                            BlockMatcher(),
                        ) as List<Matcher<Token>>,
                    ),
                ),
            ) as List<Matcher<Token>>,
        )
}
