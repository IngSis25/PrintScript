package rules

import builders.NodeBuilder
import main.kotlin.lexer.Token
import main.kotlin.parser.ParseResult
import matchers.FlexibleExpressionMatcher
import parser.matchers.Matcher
import parser.rules.ParserRule
import types.AssignmentType
import types.IdentifierType
import types.ModifierType
import types.NumberType
import types.PunctuationType
import types.StringType

/**
 * Regla flexible para declaraciones de variables que puede manejar:
 * - let variable = "value";
 * - let variable: string = "value";
 * - let variable: number = 123;
 * - let variable: number = 5 * 5 - 8;
 *
 * Fácilmente expandible para nuevos tipos y patrones.
 */
class VariableDeclarationRule(
    override val builder: NodeBuilder,
) : ParserRule {
    override val matcher: Matcher<*> =
        object : Matcher<List<Token>> {
            override fun match(
                tokens: List<Token>,
                pos: Int,
            ): ParseResult<List<Token>>? {
                if (pos + 2 >= tokens.size) return null

                // Debe empezar con let/var
                if (tokens[pos].type != ModifierType) return null

                // Debe tener un identificador
                if (tokens[pos + 1].type != IdentifierType) return null

                var currentPos = pos + 2
                val collected = mutableListOf<Token>()
                collected.add(tokens[pos]) // let/var
                collected.add(tokens[pos + 1]) // identifier

                // Caso 1: let variable = value; (sin tipo)
                if (currentPos < tokens.size && tokens[currentPos].type == AssignmentType) {
                    return matchSimpleDeclaration(tokens, currentPos, collected)
                }

                // Caso 2: let variable: type = value; (con tipo)
                if (currentPos + 2 < tokens.size &&
                    tokens[currentPos].type == PunctuationType &&
                    tokens[currentPos].value == ":" &&
                    (tokens[currentPos + 1].type == StringType || tokens[currentPos + 1].type == NumberType) &&
                    tokens[currentPos + 2].type == AssignmentType
                ) {
                    return matchTypedDeclaration(tokens, currentPos, collected)
                }

                // Caso 3: let variable: type; (declaración sin inicialización)
                if (currentPos + 1 < tokens.size &&
                    tokens[currentPos].type == PunctuationType &&
                    tokens[currentPos].value == ":" &&
                    (tokens[currentPos + 1].type == StringType || tokens[currentPos + 1].type == NumberType) &&
                    currentPos + 2 < tokens.size &&
                    tokens[currentPos + 2].type == PunctuationType &&
                    tokens[currentPos + 2].value == ";"
                ) {
                    return matchDeclarationWithoutInit(tokens, currentPos, collected)
                }

                return null
            }

            private fun matchSimpleDeclaration(
                tokens: List<Token>,
                currentPos: Int,
                collected: MutableList<Token>,
            ): ParseResult<List<Token>>? {
                collected.add(tokens[currentPos]) // =
                var pos = currentPos + 1

                // Buscar la expresión usando el matcher flexible
                val expressionMatcher = FlexibleExpressionMatcher()
                val expressionResult = expressionMatcher.match(tokens, pos) ?: return null
                when (expressionResult) {
                    is ParseResult.Success -> {
                        collected.addAll(expressionResult.node)
                        pos = expressionResult.nextPosition
                    }
                    is ParseResult.Failure -> return null
                }

                // Debe terminar con ;
                if (pos < tokens.size && tokens[pos].type == PunctuationType) {
                    collected.add(tokens[pos])
                    return ParseResult.Success(collected, pos + 1)
                }
                return null
            }

            private fun matchTypedDeclaration(
                tokens: List<Token>,
                currentPos: Int,
                collected: MutableList<Token>,
            ): ParseResult<List<Token>>? {
                collected.add(tokens[currentPos]) // :
                collected.add(tokens[currentPos + 1]) // string/number
                collected.add(tokens[currentPos + 2]) // =
                var pos = currentPos + 3

                // Buscar la expresión usando el matcher flexible
                val expressionMatcher = FlexibleExpressionMatcher()
                val expressionResult = expressionMatcher.match(tokens, pos) ?: return null
                when (expressionResult) {
                    is ParseResult.Success -> {
                        collected.addAll(expressionResult.node)
                        pos = expressionResult.nextPosition
                    }
                    is ParseResult.Failure -> return null
                }

                // Debe terminar con ;
                if (pos < tokens.size && tokens[pos].type == PunctuationType) {
                    collected.add(tokens[pos])
                    return ParseResult.Success(collected, pos + 1)
                }
                return null
            }

            private fun matchDeclarationWithoutInit(
                tokens: List<Token>,
                currentPos: Int,
                collected: MutableList<Token>,
            ): ParseResult<List<Token>>? {
                collected.add(tokens[currentPos]) // :
                collected.add(tokens[currentPos + 1]) // string/number
                collected.add(tokens[currentPos + 2]) // ;

                return ParseResult.Success(collected, currentPos + 3)
            }
        }
}
