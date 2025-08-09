package parser.rules

import lexer.AssignmentType
import lexer.ModifierType
import main.kotlin.lexer.IdentifierType
import main.kotlin.lexer.LiteralString
import main.kotlin.lexer.PunctuationType
import parser.matchers.SequenceMatcher
import parser.matchers.TokenMatcher

class VariableDeclarationRule : ParserRule {
    val matcher = SequenceMatcher(
        listOf(
            TokenMatcher(ModifierType),
            TokenMatcher(IdentifierType),
            TokenMatcher(AssignmentType),
            TokenMatcher(LiteralString),
            TokenMatcher(PunctuationType)
        )
    )
}

