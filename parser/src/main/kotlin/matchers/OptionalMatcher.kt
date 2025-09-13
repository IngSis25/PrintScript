package matchers

import main.kotlin.lexer.Token
import main.kotlin.parser.ParseResult
import parser.matchers.Matcher

class OptionalMatcher<T>(
    private val inner: Matcher<T>
) : Matcher<T?> {
    override fun match(tokens: List<Token>, pos: Int): ParseResult<T?>? {
        val result = inner.match(tokens, pos)

        return when (result) {
            is ParseResult.Success -> ParseResult.Success(result.node, result.nextPosition)
            is ParseResult.Failure, null -> ParseResult.Success(null, pos) // no avanza, pero no es error
        }
    }
}

//para el else opcional, si encuentra un else lo procesa, pero si no hay else no tira error
//si no hay else arma el nodo solo con if
