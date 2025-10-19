package builders

import main.kotlin.lexer.Token
import org.example.LiteralNumber
import org.example.LiteralString
import org.example.ast.*
import types.IdentifierType
import types.LiteralBoolean
import types.NumberType
import types.StringType

/**
 * Builder flexible para declaraciones de variables que puede manejar:
 * - Literales simples (string, number)
 * - Variables
 * - Expresiones binarias de cualquier longitud
 * - Tipos explícitos o inferidos
 */
class VariableDeclarationBuilder : NodeBuilder {
    override fun buildNode(matchedTokens: List<Token>): ASTNode {
        val identifier = matchedTokens[1].value
        val identifierNode = IdentifierNode(identifier)

        // Determinar si tiene tipo explícito
        val hasType = matchedTokens.size > 3 && matchedTokens[2].value == ":"

        val varType: String
        val valueStartIndex: Int

        if (hasType) {
            // let variable: type = value;
            varType =
                when (matchedTokens[3].type) {
                    StringType -> "string"
                    NumberType -> "number"
                    else -> "unknown"
                }
            valueStartIndex = 5 // después de let, variable, :, type, =
        } else {
            // let variable = value;
            varType = inferType(matchedTokens, 3) // después de let, variable, =
            valueStartIndex = 3
        }

        // Verificar si es una declaración sin inicialización
        if (matchedTokens.size == 5 && matchedTokens[2].value == ":" && matchedTokens[4].value == ";") {
            // let variable: type; (sin inicialización)
            return VariableDeclarationNode(
                identifier = identifierNode,
                varType = varType,
                value = null, // Sin valor inicial
            )
        }

        // Verificar que tenemos tokens para el valor
        if (valueStartIndex >= matchedTokens.size - 1) {
            throw RuntimeException("No hay valor para la variable: $matchedTokens")
        }

        val valueTokens = matchedTokens.subList(valueStartIndex, matchedTokens.size - 1) // excluir ;
        val valueNode = buildValueNode(valueTokens)

        return VariableDeclarationNode(
            identifier = identifierNode,
            varType = varType,
            value = valueNode,
        )
    }

    private fun inferType(
        tokens: List<Token>,
        startIndex: Int,
    ): String {
        if (startIndex >= tokens.size) return "unknown"

        val firstValueToken = tokens[startIndex]
        return when (firstValueToken.type) {
            LiteralString -> "string"
            LiteralNumber -> "number"
            LiteralBoolean -> "boolean"
            IdentifierType -> "unknown" // no podemos inferir el tipo de una variable
            else -> "unknown"
        }
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
            LiteralBoolean -> LiteralBooleanNode(token.value.toBoolean())
            IdentifierType -> IdentifierNode(token.value)
            else -> error("Token no válido en expresión: ${token.type}")
        }

    private fun unquote(raw: String): String =
        if (raw.length >= 2 && raw.first() == '"' && raw.last() == '"') {
            raw.substring(1, raw.length - 1)
        } else {
            raw
        }
}
