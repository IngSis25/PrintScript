package rules

import main.kotlin.lexer.Token
import parser.rules.ParserRule

data class MatchedRule(
    val rule: ParserRule,
    val matchedTokens: List<Token>
)
