package rules

import builders.NodeBuilder
import main.kotlin.lexer.Token
import main.kotlin.parser.ParseResult
import matchers.FlexibleExpressionMatcher
import parser.matchers.Matcher
import types.AssignmentType
import types.IdentifierType
import types.PunctuationType

/**
 * Regla para asignaciones de variables:
 * - variable = value;
 * - variable = expression;
 */
class AssignmentRule(
    override val builder: NodeBuilder,
) : ParserRule {
    override val matcher: Matcher<*> =
        object : Matcher<List<Token>> {
            override fun match(
                tokens: List<Token>,
                pos: Int,
            ): ParseResult<List<Token>>? {
                // Debe empezar con un identificador
                if (tokens[pos].type != IdentifierType) return null

                // Debe tener un operador de asignación
                if (pos + 1 >= tokens.size || tokens[pos + 1].type != AssignmentType) return null

                val collected = mutableListOf<Token>()
                collected.add(tokens[pos]) // identifier
                collected.add(tokens[pos + 1]) // =

                var currentPos = pos + 2

                // Buscar la expresión usando el matcher flexible
                val expressionMatcher = FlexibleExpressionMatcher()
                val expressionResult = expressionMatcher.match(tokens, currentPos) ?: return null

                when (expressionResult) {
                    is ParseResult.Success -> {
                        collected.addAll(expressionResult.node)
                        currentPos = expressionResult.nextPosition
                    }
                    is ParseResult.Failure -> return null
                }

                // Debe terminar con ;
                if (currentPos < tokens.size &&
                    tokens[currentPos].type == PunctuationType &&
                    tokens[currentPos].value == ";"
                ) {
                    collected.add(tokens[currentPos])
                    return ParseResult.Success(collected, currentPos + 1)
                }

                return null
            }
        }
}
