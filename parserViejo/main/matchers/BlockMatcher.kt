package matchers

import main.kotlin.lexer.Token
import main.kotlin.parser.ParseResult
import parser.matchers.Matcher
import types.PunctuationType

class BlockMatcher : Matcher<List<Token>> {
    override fun match(
        tokens: List<Token>,
        pos: Int,
    ): ParseResult<List<Token>>? {
        if (pos >= tokens.size) return null

        val start = tokens[pos]
        if (start.type != PunctuationType || start.value != "{") return null

        var currentPos = pos + 1
        val collected = mutableListOf<Token>()

        while (currentPos < tokens.size) {
            val token = tokens[currentPos]

            if (token.type == PunctuationType && token.value == "}") {
                // Cerramos el bloque
                return ParseResult.Success(collected, currentPos + 1)
            }

            collected.add(token)
            currentPos++
        }

        // Si llegamos al final sin encontrar "}", es un error de sintaxis
        return ParseResult.Failure("Se esperaba '}' para cerrar el bloque", currentPos)
    }
}
