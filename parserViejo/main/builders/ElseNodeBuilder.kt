package builders

import main.kotlin.lexer.Token
import main.kotlin.parser.DefaultParser
import org.example.ast.ASTNode
import org.example.ast.BlockNode
import org.example.ast.ElseNode

class ElseNodeBuilder(
    private val parser: DefaultParser,
) : NodeBuilder {
    override fun buildNode(input: List<Token>): ASTNode {
        val blockTokens = input.drop(1)

        // parseamos los tokens del bloque para obtener ASTNodes
        val statements: List<ASTNode> = parser.parse(blockTokens)

        return ElseNode(BlockNode(statements))
    }
}
