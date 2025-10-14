package builders

import main.kotlin.lexer.Token
import org.example.LiteralNumber
import org.example.LiteralString
import org.example.ast.*
import types.IdentifierType

/**
 * Builder para asignaciones de variables:
 * - variable = value;
 * - variable = expression;
 */
class AssignmentBuilder : NodeBuilder {
    override fun buildNode(matchedTokens: List<Token>): ASTNode {
        val identifier = matchedTokens[0].value
        val identifierNode = IdentifierNode(identifier)

        // Los tokens de la expresión están desde la posición 2 hasta el final (excluyendo ;)
        val expressionTokens = matchedTokens.subList(2, matchedTokens.size - 1)
        val valueNode = buildValueNode(expressionTokens)

        return AssignmentNode(
            identifier = identifierNode,
            value = valueNode,
        )
    }

    private fun buildValueNode(tokens: List<Token>): ASTNode {
        when (tokens.size) {
            1 -> {
                // Un solo token (literal o variable)
                return tokenToPrimary(tokens[0])
            }
            else -> {
                // Expresión binaria (cualquier longitud)
                return buildBinaryExpression(tokens)
            }
        }
    }

    private fun buildBinaryExpression(tokens: List<Token>): ASTNode {
        if (tokens.size < 3) {
            error("Expresión binaria debe tener al menos 3 tokens")
        }

        // Construir de izquierda a derecha: A op B op C = (A op B) op C
        var result = tokenToPrimary(tokens[0])

        var i = 1
        while (i < tokens.size - 1) {
            val operator = tokens[i].value
            val right = tokenToPrimary(tokens[i + 1])
            result = BinaryOpNode(result, operator, right)
            i += 2
        }

        return result
    }

    private fun tokenToPrimary(token: Token): ASTNode =
        when (token.type) {
            LiteralString -> LiteralNode(unquote(token.value), LiteralString)
            LiteralNumber -> LiteralNode(token.value, LiteralNumber)
            IdentifierType -> IdentifierNode(token.value)
            else -> error("Tipo de token no soportado: ${token.type}")
        }

    private fun unquote(value: String): String =
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value.substring(1, value.length - 1)
        } else {
            value
        }
}
