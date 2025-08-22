package builders

import main.kotlin.lexer.Token
import main.kotlin.parser.ASTNode

interface NodeBuilder {
    fun buildNode(input: List<Token>): ASTNode
}