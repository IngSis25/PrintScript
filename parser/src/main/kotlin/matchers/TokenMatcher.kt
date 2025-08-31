package matchers

import main.kotlin.lexer.Token
import main.kotlin.parser.ParseResult
import org.example.TokenType
import parser.matchers.Matcher

// devuelve un token cuando matchea
class TokenMatcher(
    private val type: TokenType,
) : Matcher<Token> {
    override fun match(
        tokens: List<Token>,
        pos: Int,
    ): ParseResult<Token>? {
        if (pos >= tokens.size) return null

        val token = tokens[pos]

        if (token.type != type) return null

        // return ParseResult(token, pos + 1)
        return ParseResult.Success(token, pos + 1)
    }
}
