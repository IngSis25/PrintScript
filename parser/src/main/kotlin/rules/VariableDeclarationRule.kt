package parser.rules

import builders.NodeBuilder
import main.kotlin.lexer.IdentifierType
import org.example.LiteralString
import matchers.SequenceMatcher
import matchers.TokenMatcher
import parser.matchers.Matcher
import types.AssignmentType
import types.ModifierType
import types.PunctuationType

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
