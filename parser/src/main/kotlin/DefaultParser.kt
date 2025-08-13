package parser

import main.kotlin.lexer.Token
import main.kotlin.parser.ASTNode
import main.kotlin.parser.ParseResult
import main.kotlin.parser.Parser
import rules.RuleMatcher

class DefaultParser(
    private val ruleMatcher: RuleMatcher
) : Parser {

    override fun parse(tokens: List<Token>): ParseResult<ASTNode> {
        return parseRecursive(tokens, 0)
            ?: ParseResult.Failure("No parse result could be produced", 0)
    }

    private fun parseRecursive(tokens: List<Token>, pos: Int): ParseResult<ASTNode>? {
        if (pos >= tokens.size) return null

        return when (val matchResult = ruleMatcher.matchNext(tokens, pos)) { //si nunca voy a tener mas opciones de ParseResult
            is ParseResult.Success<*> -> {
                val node = matchResult.node.rule.buildNode(matchResult.node.matchedTokens)
                val nextResult = parseRecursive(tokens, matchResult.nextPosition)
                ParseResult.Success(node, nextResult?.nextPosition ?: matchResult.nextPosition)
            }
            is ParseResult.Failure -> matchResult
        }
    }
}
