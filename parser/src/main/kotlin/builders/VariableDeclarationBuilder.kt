package builders

import main.kotlin.lexer.LiteralString
import main.kotlin.lexer.Token
import org.example.ast.ASTNode
import org.example.ast.IdentifierNode
import org.example.ast.LiteralNode
import org.example.ast.VariableDeclarationNode

class VariableDeclarationBuilder : NodeBuilder {
    override fun buildNode(matchedTokens: List<Token>): ASTNode {
        val identToken = matchedTokens[1]
        val valueToken = matchedTokens[3]

        val identNode = IdentifierNode(identToken.value)

        val raw = valueToken.value
        val unquoted =
            if (raw.length >= 2 && raw.first() == '"' && raw.last() == '"') {
                raw.substring(1, raw.length - 1)
            } else {
                raw
            }

        val literalNode =
            LiteralNode(
                value = unquoted,
                literalType = LiteralString,
            )
        return VariableDeclarationNode(
            identifier = identNode,
            varType = "string", // hardcodeado por ahora en string, desp lo arreglamos para poner mas
            value = literalNode,
        )
    }
}
