package rules

import builders.NodeBuilder
import main.kotlin.lexer.Token
import main.kotlin.parser.ParseResult
import matchers.BlockMatcher
import org.example.ast.ASTNode
import org.example.ast.BlockNode
import parser.matchers.Matcher
import parser.rules.ParserRule

class BlockRule(
    override val builder: NodeBuilder,
    private val ruleMatcher: RuleMatcher,
) : ParserRule {
    override val matcher: Matcher<*> = BlockMatcher()

    fun buildBlock(tokens: List<Token>): BlockNode {
        val statements = mutableListOf<ASTNode>()
        var pos = 0
        while (pos < tokens.size) {
            val result = ruleMatcher.matchNext(tokens, pos)
            when (result) {
                is ParseResult.Success -> {
                    statements.add(
                        result.node.rule.builder
                            .buildNode(result.node.matchedTokens),
                    )
                    pos = result.nextPosition
                }
                is ParseResult.Failure -> throw RuntimeException(result.message)
            }
        }
        return BlockNode(statements)
    }
}
