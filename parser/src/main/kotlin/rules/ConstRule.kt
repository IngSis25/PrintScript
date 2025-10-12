package parser.rules

import builders.NodeBuilder
import main.kotlin.lexer.Token
import main.kotlin.parser.ParseResult
import matchers.FlexibleExpressionMatcher
import parser.matchers.Matcher
import types.AssignmentType
import types.IdentifierType
import types.ModifierType
import types.PunctuationType

/**
 * Regla para declaraciones const
 * Forma: const <id> ( : <type> )? = <expr> ;
 * Nota: el inicializador es OBLIGATORIO en const.
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
                var i = pos
                if (i >= tokens.size) return null

                // 1) Keyword 'const'
                val t0 = tokens[i]
                if (t0.type != ModifierType || t0.value != "const") return null

                val collected = mutableListOf<Token>()
                collected.add(t0)
                i++

                // 2) Identifier (variable name)
                if (i >= tokens.size || tokens[i].type != IdentifierType) return null
                collected.add(tokens[i])
                i++

                // 3) Optional ': <type>'
                if (i < tokens.size &&
                    tokens[i].type == PunctuationType &&
                    tokens[i].value == ":"
                ) {
                    collected.add(tokens[i]) // ':'
                    i++

                    // tipo esperado (usamos IdentifierType por simplicidad)
                    if (i >= tokens.size || tokens[i].type != IdentifierType) return null
                    collected.add(tokens[i]) // type name
                    i++
                }

                // 4) '=' obligatorio
                if (i >= tokens.size || tokens[i].type != AssignmentType) return null
                collected.add(tokens[i]) // '='
                i++

                // 5) Expresión obligatoria
                val exprMatcher = FlexibleExpressionMatcher()
                val exprResult = exprMatcher.match(tokens, i) ?: return null

                when (exprResult) {
                    is ParseResult.Success -> {
                        collected.addAll(exprResult.node)
                        i = exprResult.nextPosition
                    }
                    is ParseResult.Failure -> return null
                }

                // 6) ';' obligatorio
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
