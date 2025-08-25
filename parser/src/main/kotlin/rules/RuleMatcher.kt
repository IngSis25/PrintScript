package rules

import main.kotlin.lexer.Token
import main.kotlin.parser.ParseResult
import parser.rules.ParserRule

class RuleMatcher(
    private val rules: List<ParserRule>,
) {
    fun matchNext(
        tokens: List<Token>,
        pos: Int,
    ): ParseResult<MatchedRule> {
        for (rule in rules) {
            // intento aplicar cada regla
            val result = rule.matcher.match(tokens, pos) ?: continue

            when (result) {
                is ParseResult.Success<*> -> {
                    val end = result.nextPosition
                    val slice = tokens.subList(pos, end)
                    return ParseResult.Success(
                        MatchedRule(rule, slice),
                        end,
                    )
                }
                is ParseResult.Failure -> {
                    continue
                }
            }
        }
        return ParseResult.Failure("No matching rule at position $pos: ${tokens[pos]}", pos)
    }
}
