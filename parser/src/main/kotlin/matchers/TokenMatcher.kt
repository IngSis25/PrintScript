import main.kotlin.lexer.Token
import main.kotlin.parser.ParseResult
import org.example.TokenType
import parser.matchers.Matcher

class TokenMatcher(
    private val type: TokenType,
    private val expectedValue: String? = null //si quiero matchear un valor especifico dentro de un type
) : Matcher<Token> {
    override fun match(tokens: List<Token>, pos: Int): ParseResult<Token>? {
        if (pos >= tokens.size) return null

        val token = tokens[pos]

        if (token.type != type) return null
        if (expectedValue != null && token.value != expectedValue) return null

        return ParseResult.Success(token, pos + 1)
    }
}
