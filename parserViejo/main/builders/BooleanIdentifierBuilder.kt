package builders

import main.kotlin.lexer.Token
import org.example.ast.ASTNode
import org.example.ast.IdentifierBooleanNode

class BooleanIdentifierBuilder : NodeBuilder {
    override fun buildNode(input: List<Token>): ASTNode {
        if (input.size != 1) {
            throw IllegalArgumentException("BooleanIdentifierBuilder expects exactly one token")
        }

        val token = input[0]
        return IdentifierBooleanNode(token.value)
    }
}
