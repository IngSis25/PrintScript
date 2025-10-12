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
 * Regla para declaraciones let
 * Forma: let <id> ( : <type> )? ( = <expr> )? ;
 * Nota: el inicializador es opcional en let.
 */
class LetRule(
    override val builder: NodeBuilder,
) : ParserRule {
    override val matcher: Matcher<*> =
        object : Matcher<List<Token>> {
            override fun match(
                tokens: List<Token>,
                pos: Int,
            ): ParseResult<List<Token>>? {
                var i = pos
                if (i >= tokens.size) return null

                // 1) Keyword 'let'
                val t0 = tokens[i]
                if (t0.type != IdentifierType || t0.value != "let") return null

                val collected = mutableListOf<Token>()
                collected.add(t0)
                i++

                // 2) Identifier
                if (i >= tokens.size || tokens[i].type != IdentifierType) return null
                collected.add(tokens[i])
                i++

                if (i < tokens.size &&
                    tokens[i].type == PunctuationType &&
                    tokens[i].value == ":"
                ) {
                    collected.add(tokens[i]) // ':'
                    i++

                    if (i >= tokens.size || tokens[i].type != IdentifierType) return null
                    collected.add(tokens[i]) // type name (e.g., number|string|boolean)
                    i++
                }

                if (i < tokens.size && tokens[i].type == AssignmentType) {
                    collected.add(tokens[i]) // '='
                    i++

                    val exprMatcher = FlexibleExpressionMatcher()
                    val exprResult = exprMatcher.match(tokens, i) ?: return null

                    when (exprResult) {
                        is ParseResult.Success -> {
                            collected.addAll(exprResult.node)
                            i = exprResult.nextPosition
                        }
                        is ParseResult.Failure -> return null
                    }
                }

                // 5) Termina con ';'
                if (i < tokens.size &&
                    tokens[i].type == PunctuationType &&
                    tokens[i].value == ";"
                ) {
                    collected.add(tokens[i])
                    return ParseResult.Success(collected, i + 1)
                }

                return null
            }
        }
}
