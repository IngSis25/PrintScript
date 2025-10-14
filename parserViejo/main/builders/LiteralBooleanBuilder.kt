package builders

import main.kotlin.lexer.Token
import org.example.ast.ASTNode
import org.example.ast.LiteralBooleanNode

class LiteralBooleanBuilder : NodeBuilder {
    override fun buildNode(input: List<Token>): ASTNode {
        if (input.size != 1) {
            throw IllegalArgumentException("LiteralBooleanBuilder expects exactly one token")
        }

        val token = input[0]
        val value =
            when (token.value) {
                "true" -> true
                "false" -> false
                else -> throw IllegalArgumentException("Invalid boolean literal: ${token.value}")
            }

        return LiteralBooleanNode(value)
    }
}
