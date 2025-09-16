package builders

import main.kotlin.lexer.Token
import main.kotlin.parser.DefaultParser
import org.example.ast.ASTNode
import org.example.ast.BlockNode

class BlockBuilder(
    private val parser: DefaultParser,
) : NodeBuilder {
    override fun buildNode(input: List<Token>): ASTNode {
        val innerTokens = input.drop(1).dropLast(1)

        val statements: List<ASTNode> = parser.parse(innerTokens)

        return BlockNode(statements)
    }
}
