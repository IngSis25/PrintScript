package builders

import main.kotlin.lexer.IdentifierType
import main.kotlin.lexer.LiteralNumber
import main.kotlin.lexer.LiteralString
import main.kotlin.lexer.OperatorType
import main.kotlin.lexer.Token
import org.example.ast.*

class PrintBuilder : NodeBuilder {
    override fun buildNode(matchedTokens: List<Token>): ASTNode {
        val openParenIndex = 1
        val closeParenIndex = matchedTokens.lastIndex - 1
        val inner = matchedTokens.subList(openParenIndex + 1, closeParenIndex) // toma lo que esta adentro de ()

        val exprNode = parseInnerExpression(inner)
        return PrintlnNode(exprNode)
    }

    // ahora tengo que convertir los tokens internos en un nodo de EXPRESION
    private fun parseInnerExpression(tokens: List<Token>): ASTNode {
        // Caso simple: UN token (literal o identificador)
        if (tokens.size == 1) return tokenToPrimary(tokens[0])

        // Caso binario: 3 tokens => A op B (ej: 12 + 8)
        if (tokens.size == 3 && tokens[1].type == OperatorType) {
            val left = tokenToPrimary(tokens[0])
            val op = tokens[1].value
            val right = tokenToPrimary(tokens[2])
            return BinaryOpNode(left, op, right)
        }
        error("Expresión no soportada dentro de println por ahora")
    }

    private fun tokenToPrimary(token: Token): ASTNode =
        when (token.type) {
            LiteralString -> LiteralNode(unquote(token.value), LiteralString)
            LiteralNumber -> LiteralNode(token.value, LiteralNumber)
            IdentifierType -> IdentifierNode(token.value)
            else -> error("Token no válido en println: ${token.type}")
        }

    private fun unquote(raw: String): String =
        if (raw.length >= 2 && raw.first() == '"' && raw.last() == '"') {
            raw.substring(1, raw.length - 1)
        } else {
            raw
        }
}
