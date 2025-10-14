package builders

import main.kotlin.lexer.Token
import org.example.ast.ASTNode

interface NodeBuilder {
    fun buildNode(input: List<Token>): ASTNode
}
