package builders

import main.kotlin.lexer.Token
import org.example.LiteralNumber
import org.example.LiteralString
import org.example.ast.*
import types.BooleanType
import types.IdentifierType
import types.LiteralBoolean
import types.NumberType
import types.StringType

/**
 * Builder para declaraciones const que puede manejar:
 * - const variable = value;
 * - const variable: type = value;
 */
class ConstBuilder : NodeBuilder {
    override fun buildNode(matchedTokens: List<Token>): ASTNode {
        val identifier = matchedTokens[1].value
        val identifierNode = IdentifierNode(identifier)

        // Determinar si tiene tipo explícito
        val hasType = matchedTokens.size > 3 && matchedTokens[2].value == ":"

        val varType: String
        val valueStartIndex: Int

        if (hasType) {
            // const variable: type = value;
            varType =
                when (matchedTokens[3].type) {
                    StringType -> "string"
                    NumberType -> "number"
                    BooleanType -> "boolean"
                    else -> "unknown"
                }
            valueStartIndex = 5 // después de const, variable, :, type, =
        } else {
            // const variable = value;
            varType = "const" // Mantener "const" como tipo para compatibilidad
            valueStartIndex = 3
        }

        // Verificar que tenemos tokens para el valor
        if (valueStartIndex >= matchedTokens.size - 1) {
            throw IllegalArgumentException("ConstBuilder: No hay suficientes tokens para el valor")
        }

        // Construir el valor (puede ser literal o expresión compleja)
        val valueTokens = matchedTokens.subList(valueStartIndex, matchedTokens.size - 1) // sin el ;
        val value = buildValue(valueTokens)

        return VariableDeclarationNode(
            identifier = identifierNode,
            varType = varType,
            value = value,
        )
    }

    private fun buildValue(valueTokens: List<Token>): ASTNode =
        when {
            valueTokens.size == 1 -> {
                // Valor simple (literal o identificador)
                val token = valueTokens[0]
                when (token.type) {
                    LiteralString, LiteralNumber -> LiteralNode(token.value, token.type)
                    LiteralBoolean -> {
                        val value =
                            when (token.value) {
                                "true" -> true
                                "false" -> false
                                else -> throw IllegalArgumentException("Invalid boolean literal: ${token.value}")
                            }
                        LiteralBooleanNode(value)
                    }
                    IdentifierType -> IdentifierNode(token.value)
                    else -> throw IllegalArgumentException("Tipo de token no soportado: ${token.type}")
                }
            }
            else -> {
                // Expresión compleja - usar ExpressionBuilder
                val expressionBuilder = ExpressionBuilder()
                expressionBuilder.buildNode(valueTokens)
            }
        }

    private fun inferType(
        tokens: List<Token>,
        startIndex: Int,
    ): String {
        if (startIndex >= tokens.size) return "unknown"
        return when (tokens[startIndex].type) {
            LiteralString -> "string"
            LiteralNumber -> "number"
            else -> "const" // default para const
        }
    }
}
