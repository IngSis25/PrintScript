package parser.rules

import builders.NodeBuilder
import lexer.AssignmentType
import lexer.ModifierType
import main.kotlin.lexer.IdentifierType
import main.kotlin.lexer.LiteralString
import main.kotlin.lexer.PunctuationType
import matchers.SequenceMatcher
import matchers.TokenMatcher
import parser.matchers.Matcher

class VariableDeclarationRule(
    override val builder: NodeBuilder,
) : ParserRule {
    override val matcher: Matcher<*> =
        SequenceMatcher(
            listOf(
                TokenMatcher(ModifierType),
                TokenMatcher(IdentifierType),
                TokenMatcher(AssignmentType),
                TokenMatcher(LiteralString),
                TokenMatcher(PunctuationType),
            ),
        )
}
