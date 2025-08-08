package parser.matchers

import main.kotlin.lexer.Token
import main.kotlin.parser.ParseResult

class SequenceMatcher(
    private val matchers: List<Matcher<*>>
) : Matcher<List<Any?>> {

    override fun match(tokens: List<Token>, pos: Int): ParseResult<List<Any?>>? {
        var currentPos = pos
        val results = mutableListOf<Any?>()

        for (matcher in matchers) {
            val result = matcher.match(tokens, currentPos) ?: return null
            results.add(result.node)
            currentPos = result.nextPosition
        }

        return ParseResult(results, currentPos)
    }
}