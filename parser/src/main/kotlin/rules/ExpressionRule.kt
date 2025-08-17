package parser.rules

import main.kotlin.lexer.LiteralNumber
import main.kotlin.lexer.OperatorType
import main.kotlin.lexer.Token
import main.kotlin.parser.ASTNode
import main.kotlin.parser.BinaryOpNode
import main.kotlin.parser.LiteralNode
import matchers.SequenceMatcher
import matchers.TokenMatcher

class ExpressionRule: ParserRule {
    override val matcher = SequenceMatcher(
        listOf(
            TokenMatcher(LiteralNumber),
            TokenMatcher(OperatorType),
            TokenMatcher(LiteralNumber)
        )
    )

    override fun buildNode(matchedTokens: List<Token>): ASTNode {
        val left = LiteralNode(matchedTokens[0].value, LiteralNumber)
        val operator = matchedTokens[1].value
        val right = LiteralNode(matchedTokens[2].value, LiteralNumber)
        return BinaryOpNode(left, operator, right)
    }
}