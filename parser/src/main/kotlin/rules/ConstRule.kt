package rules

import builders.NodeBuilder
import main.kotlin.lexer.Token
import main.kotlin.parser.ParseResult
import matchers.FlexibleExpressionMatcher
import parser.matchers.Matcher
import parser.rules.ParserRule
import types.AssignmentType
import types.BooleanType
import types.IdentifierType
import types.ModifierType
import types.NumberType
import types.PunctuationType
import types.StringType

/**
 * Regla para declaraciones const que puede manejar:
 * - const variable = value;
 * - const variable: type = value;
 */
class ConstRule(
    override val builder: NodeBuilder,
) : ParserRule {
    override val matcher: Matcher<*> =
        object : Matcher<List<Token>> {
            override fun match(
                tokens: List<Token>,
                pos: Int,
            ): ParseResult<List<Token>>? {
                if (pos + 2 >= tokens.size) return null

                // Debe empezar con const
                if (tokens[pos].type != ModifierType || tokens[pos].value != "const") return null

                // Debe tener un identificador
                if (tokens[pos + 1].type != IdentifierType) return null

                var currentPos = pos + 2
                val collected = mutableListOf<Token>()
                collected.add(tokens[pos]) // const
                collected.add(tokens[pos + 1]) // identifier

                // Caso 1: const variable = value; (sin tipo)
                if (currentPos < tokens.size && tokens[currentPos].type == AssignmentType) {
                    return matchSimpleConstDeclaration(tokens, currentPos, collected)
                }

                // Caso 2: const variable: type = value; (con tipo)
                if (currentPos + 2 < tokens.size &&
                    tokens[currentPos].type == PunctuationType &&
                    tokens[currentPos].value == ":" &&
                    (
                        tokens[currentPos + 1].type == StringType ||
                            tokens[currentPos + 1].type == NumberType ||
                            tokens[currentPos + 1].type == BooleanType
                    ) &&
                    tokens[currentPos + 2].type == AssignmentType
                ) {
                    return matchTypedConstDeclaration(tokens, currentPos, collected)
                }

                return null
            }

            private fun matchSimpleConstDeclaration(
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

            private fun matchTypedConstDeclaration(
                tokens: List<Token>,
                currentPos: Int,
                collected: MutableList<Token>,
            ): ParseResult<List<Token>>? {
                collected.add(tokens[currentPos]) // :
                collected.add(tokens[currentPos + 1]) // string/number/boolean
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
        }
}
