package parser

import main.kotlin.lexer.Token
import main.kotlin.lexer.TokenProvider
import main.kotlin.parser.ASTNode
import main.kotlin.parser.ParseResult
import main.kotlin.parser.Parser
import parser.rules.ParserRule

class DefaultParser(
    private val rules: List<ParserRule>
) : Parser {
    override fun parse(tokens: List<Token>): ParseResult<ASTNode>? {
        return parseRecursive(tokens, 0)
    }

    private fun parseRecursive(tokens: List<Token>, pos: Int): ParseResult<ASTNode>? {
        if (pos >= tokens.size) return null

        for (rule in rules) {
            val result = rule.matcher.match(tokens, pos)
            if (result != null) {
                val node = buildAstNodeForRule(rule, tokens.subList(pos, result.nextPosition))
                // Intentar parsear lo que queda recursivamente (si querés hacer un árbol completo)
                val nextResult = parseRecursive(tokens, result.nextPosition)
                return ParseResult(node, nextResult?.nextPosition ?: result.nextPosition)
            }
        }

        return null
    }

    private fun buildAstNodeForRule(rule: ParserRule, subList: List<Token>): ASTNode {

    }
}
