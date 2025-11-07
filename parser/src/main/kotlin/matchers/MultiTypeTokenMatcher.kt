package matchers

import main.kotlin.lexer.Token
import main.kotlin.parser.ParseResult
import parser.matchers.Matcher

// chequea si el token pertenecee a alguno de los tipos que se le pasan
class MultiTypeTokenMatcher(
    private val types: List<TokenType>,
) : Matcher<Token> {
    override fun match(
        tokens: List<Token>,
        pos: Int,
    ): ParseResult<Token>? {
        if (pos >= tokens.size) return null

        val token = tokens[pos]

        // Si el token es de alguno de los tipos indicados, matchea
        if (types.any { it == token.type }) {
            return ParseResult.Success(token, pos + 1)
        }

        return null
    }
}
