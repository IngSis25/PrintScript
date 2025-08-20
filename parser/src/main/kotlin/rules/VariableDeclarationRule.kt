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

    override fun buildNode(matchedTokens: List<Token>): ASTNode {
        val identToken = matchedTokens[1]
        val valueToken = matchedTokens[3]

        val identNode = IdentifierNode(identToken.value)

        val raw = valueToken.value
        val unquoted =
            if (raw.length >= 2 && raw.first() == '"' && raw.last() == '"') {
                raw.substring(1, raw.length - 1)
            } else {
                raw
            }

        val literalNode =
            LiteralNode(
                value = unquoted,
                literalType = main.kotlin.lexer.LiteralString,
            )
        return VariableDeclarationNode(
            identifier = identNode,
            varType = "string", // hardcodeado por ahora en string, desp lo arreglamos para poner mas
            value = literalNode,
        )
    }
}
