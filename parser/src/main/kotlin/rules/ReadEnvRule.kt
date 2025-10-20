package rules

import builders.NodeBuilder
import main.kotlin.lexer.Token
import main.kotlin.parser.ParseResult
import parser.matchers.Matcher
import types.IdentifierType
import types.PunctuationType
import types.StringType

class ReadEnvRule(
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

                val collected = mutableListOf<Token>()

                // 1) 'readEnv' (tokenizado como Identifier)
                val t0 = tokens[i]
                if (t0.type != IdentifierType || t0.value != "readEnv") return null
                collected.add(t0)
                i++

                // 2) '('
                if (i >= tokens.size ||
                    tokens[i].type != PunctuationType ||
                    tokens[i].value != "("
                ) {
                    return null
                }
                collected.add(tokens[i])
                i++

                // 3) STRING_LITERAL (nombre de la variable de entorno)
                if (i >= tokens.size || tokens[i].type != StringType) return null
                collected.add(tokens[i])
                i++

                // 4) ')'
                if (i >= tokens.size ||
                    tokens[i].type != PunctuationType ||
                    tokens[i].value != ")"
                ) {
                    return null
                }
                collected.add(tokens[i])
                i++

                // Éxito: devolvemos los 4 tokens y la próxima posición
                return ParseResult.Success(collected, i)
            }
        }
}
