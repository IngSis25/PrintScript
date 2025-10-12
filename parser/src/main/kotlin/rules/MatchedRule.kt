package rules

import main.kotlin.lexer.Token

data class MatchedRule(
    val rule: ParserRule,
    val matchedTokens: List<Token>,
)
