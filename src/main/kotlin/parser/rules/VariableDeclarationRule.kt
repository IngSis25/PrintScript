package parser.rules

import main.kotlin.lexer.IdentifierType
import main.kotlin.lexer.LiteralString
import parser.matchers.SequenceMatcher
import parser.matchers.TokenMatcher

class VariableDeclarationRule : ParserRule {
    override val matcher = SequenceMatcher(
        listOf(
            TokenMatcher(ModifierType),
            TokenMatcher(IdentifierType),
            TokenMatcher(AssignmentType),
            TokenMatcher(LiteralString)
        )
    )
}