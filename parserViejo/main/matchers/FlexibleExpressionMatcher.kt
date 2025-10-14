package matchers

import main.kotlin.lexer.Token
import main.kotlin.parser.ParseResult
import org.example.LiteralNumber
import org.example.LiteralString
import parser.matchers.Matcher
import types.IdentifierType
import types.LiteralBoolean
import types.OperatorType

/**
 * Matcher flexible para expresiones que puede expandirse fácilmente.
 * Maneja literales, variables y expresiones binarias de cualquier longitud.
 */
class FlexibleExpressionMatcher : Matcher<List<Token>> {
    override fun match(
        tokens: List<Token>,
        pos: Int,
    ): ParseResult<List<Token>>? {
        if (pos >= tokens.size) return null

        val collected = mutableListOf<Token>()
        var currentPos = pos

        // Primero intentar expresión binaria, luego un solo token
        val binaryResult = matchBinaryExpression(tokens, pos)
        if (binaryResult != null) {
            return binaryResult
        }

        // Si no hay expresión binaria, verificar si es un solo token
        if (isPrimaryToken(tokens[currentPos])) {
            collected.add(tokens[currentPos])
            return ParseResult.Success(collected, currentPos + 1)
        }

        return null
    }

    private fun isPrimaryToken(token: Token): Boolean =
        when (token.type) {
            LiteralString, LiteralNumber, IdentifierType, LiteralBoolean -> true
            else -> false
        }

    private fun matchBinaryExpression(
        tokens: List<Token>,
        pos: Int,
    ): ParseResult<List<Token>>? {
        val collected = mutableListOf<Token>()
        var currentPos = pos

        // Debe empezar con un token primario
        if (currentPos >= tokens.size || !isPrimaryToken(tokens[currentPos])) {
            return null
        }

        collected.add(tokens[currentPos])
        currentPos++

        // Buscar pares de operador + token primario
        while (currentPos + 1 < tokens.size) {
            val operator = tokens[currentPos]
            val operand = tokens[currentPos + 1]

            if (operator.type == OperatorType && isPrimaryToken(operand)) {
                collected.add(operator)
                collected.add(operand)
                currentPos += 2
            } else {
                break
            }
        }

        // Debe tener al menos un operador para ser una expresión binaria
        if (collected.size < 3) return null

        return ParseResult.Success(collected, currentPos)
    }
}
