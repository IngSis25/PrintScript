package matchers

import main.kotlin.lexer.Token
import main.kotlin.parser.ParseResult
import parser.matchers.Matcher
import types.IdentifierType
import types.LiteralBoolean
import types.OperatorType
import types.PunctuationType
import types.ReadEnvType

/**
 * Matcher comprehensivo para expresiones que puede manejar:
 * - Literales (string, number, boolean)
 * - Identificadores (variables)
 * - Expresiones binarias
 * - Llamadas a funciones (como readEnv("param"))
 */
class ComprehensiveExpressionMatcher : Matcher<List<Token>> {
    override fun match(
        tokens: List<Token>,
        pos: Int,
    ): ParseResult<List<Token>>? {
        if (pos >= tokens.size) return null

        // Primero intentar función call
        val functionCallResult = matchFunctionCall(tokens, pos)
        if (functionCallResult != null) {
            return functionCallResult
        }

        // Luego intentar expresión binaria
        val binaryResult = matchBinaryExpression(tokens, pos)
        if (binaryResult != null) {
            return binaryResult
        }

        // Finalmente, un solo token primario
        if (isPrimaryToken(tokens[pos])) {
            return ParseResult.Success(listOf(tokens[pos]), pos + 1)
        }

        return null
    }

    private fun isPrimaryToken(token: Token): Boolean =
        when (token.type) {
            LiteralString, LiteralNumber, IdentifierType, LiteralBoolean -> true
            else -> false
        }

    private fun matchFunctionCall(
        tokens: List<Token>,
        pos: Int,
    ): ParseResult<List<Token>>? {
        if (pos >= tokens.size) return null

        val collected = mutableListOf<Token>()
        var currentPos = pos

        // Debe empezar con un identificador o función específica (nombre de función)
        if (tokens[currentPos].type != IdentifierType && tokens[currentPos].type != ReadEnvType) return null
        collected.add(tokens[currentPos])
        currentPos++

        // Debe tener '('
        if (currentPos >= tokens.size ||
            tokens[currentPos].type != PunctuationType ||
            tokens[currentPos].value != "("
        ) {
            return null
        }
        collected.add(tokens[currentPos])
        currentPos++

        // Debe tener un argumento (literal string, number, boolean, o identificador)
        if (currentPos >= tokens.size || !isPrimaryToken(tokens[currentPos])) {
            return null
        }
        collected.add(tokens[currentPos])
        currentPos++

        // Debe tener ')'
        if (currentPos >= tokens.size ||
            tokens[currentPos].type != PunctuationType ||
            tokens[currentPos].value != ")"
        ) {
            return null
        }
        collected.add(tokens[currentPos])
        currentPos++

        return ParseResult.Success(collected, currentPos)
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
