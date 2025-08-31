package builders

import ASTNode
import main.kotlin.lexer.LiteralNumber
import main.kotlin.lexer.Token
import main.kotlin.parser.BinaryOpNode
import main.kotlin.parser.LiteralNode

class ExpressionBuilder: NodeBuilder {
    override fun buildNode(matchedTokens: List<Token>): ASTNode {
        val left = LiteralNode(matchedTokens[0].value, LiteralNumber)
        val operator = matchedTokens[1].value
        val right = LiteralNode(matchedTokens[2].value, LiteralNumber)
        return BinaryOpNode(left, operator, right)
    }
}
