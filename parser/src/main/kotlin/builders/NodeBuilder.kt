package builders

import ASTNode
import main.kotlin.lexer.Token

interface NodeBuilder {
    fun buildNode(input: List<Token>): ASTNode
}
