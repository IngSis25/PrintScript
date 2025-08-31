package parser.rules

import ASTNode
import lexer.AssignmentType
import lexer.ModifierType
import main.kotlin.lexer.IdentifierType
import main.kotlin.lexer.LiteralString
import main.kotlin.lexer.PunctuationType
import main.kotlin.lexer.Token
import main.kotlin.parser.IdentifierNode
import main.kotlin.parser.LiteralNode
import matchers.SequenceMatcher
import matchers.TokenMatcher
import parser.VariableDeclarationNode
import parser.matchers.Matcher

class VariableDeclarationRule : ParserRule {
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
