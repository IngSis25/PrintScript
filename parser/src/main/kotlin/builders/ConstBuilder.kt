package builders

import main.kotlin.lexer.Token
import org.example.ast.ASTNode
import org.example.ast.IdentifierNode
import org.example.ast.LiteralNode
import org.example.ast.VariableDeclarationNode

class ConstBuilder : NodeBuilder {
    override fun buildNode(input: List<Token>): ASTNode {
        val identifier = IdentifierNode(input[1].value)
        val value =
            LiteralNode(
                input[3].value,
                literalType = input[3].type,
            )
        return VariableDeclarationNode(identifier, "const", value)
    }
}
