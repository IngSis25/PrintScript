package builders

import DefaultParser
import main.kotlin.lexer.Token
import org.example.ast.ASTNode
import org.example.ast.BlockNode
import org.example.ast.IfNode

class IfNodeBuilder(
    private val parser: DefaultParser,
) : NodeBuilder {
    override fun buildNode(input: List<Token>): ASTNode {
        val conditionTokens = extractCondition(input)
        val conditionNode = parser.parse(conditionTokens).first() // asumimos que la condición genera un solo nodo

        val thenStart = input.indexOfFirst { it.value == "{" }
        val (thenBlock, nextPos) = parseBlock(input, thenStart)

        val elseBlock =
            if (nextPos < input.size && input[nextPos].value == "else") {
                val (block, _) = parseBlock(input, nextPos + 1)
                block
            } else {
                null
            }

        return IfNode(conditionNode, thenBlock, elseBlock)
    }

    private fun extractCondition(tokens: List<Token>): List<Token> {
        val start = tokens.indexOfFirst { it.value == "(" } + 1
        val end = tokens.indexOfFirst { it.value == ")" }
        if (start == 0 || end == -1 || end <= start) throw IllegalArgumentException("If sin condición válida")
        return tokens.subList(start, end)
    }

    private fun parseBlock(
        tokens: List<Token>,
        start: Int,
    ): Pair<BlockNode, Int> {
        if (tokens[start].value != "{") throw IllegalArgumentException("Se esperaba '{' en el bloque")
        val end = tokens.indexOfFirst { it.value == "}" && tokens.indexOf(it) > start }
        if (end == -1) throw IllegalArgumentException("Falta '}' de cierre en bloque")
        val blockTokens = tokens.subList(start + 1, end)
        val blockNode = BlockNode(parser.parse(blockTokens))
        return blockNode to end + 1
    }
}
