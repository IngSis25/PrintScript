package builders

import ASTNode
import main.kotlin.lexer.Token
import main.kotlin.parser.IdentifierNode
import main.kotlin.parser.LiteralNode
import parser.VariableDeclarationNode

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
                literalType = main.kotlin.lexer.LiteralString,
            )
        return VariableDeclarationNode(
            identifier = identNode,
            varType = "string", // hardcodeado por ahora en string, desp lo arreglamos para poner mas
            value = literalNode,
        )
    }

}
