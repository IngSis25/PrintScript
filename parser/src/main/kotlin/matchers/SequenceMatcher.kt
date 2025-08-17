package matchers

import main.kotlin.lexer.Token
import main.kotlin.parser.ParseResult
import parser.matchers.Matcher

class SequenceMatcher(
    private val parts: List<Matcher<Token>>
) : Matcher<List<Token?>> {

    override fun match(tokens: List<Token>, pos: Int): ParseResult<List<Token?>>? {
        var currentPos = pos
        val collected = ArrayList<Token>(parts.size) //para guardar los tokens que matchearon

        for (part in parts) {
            val result = part.match(tokens, currentPos) ?: return null
            when (result) {
                is ParseResult.Success -> {
                    collected.add(result.node)
                    currentPos = result.nextPosition
                }
                is ParseResult.Failure -> return null
            }
        }
        return ParseResult.Success(collected, currentPos)
        /*for (matcher in matchers) {
            val result = matcher.match(tokens, currentPos) ?: return null
            val node = result.node
            currentPos = result.nextPosition
        }

        return ParseResult(results, currentPos)*/
    }
}