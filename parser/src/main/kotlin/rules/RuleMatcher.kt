package rules

import main.kotlin.lexer.Token
import parser.rules.ParserRule
import main.kotlin.parser.ParseResult

class RuleMatcher(
    private val rules: List<ParserRule>
) {

    fun matchNext(tokens: List<Token>, pos: Int): ParseResult<MatchedRule> {
        for (rule in rules) {
            val result = rule.matcher.match(tokens, pos)
            if (result != null) {
                return ParseResult.Success(MatchedRule(rule, tokens.subList(pos, result.nextPosition)), result.nextPosition)
            }
        }
        return ParseResult.Failure("No matching rule at position $pos: ${tokens[pos]}", pos)
    }
}
