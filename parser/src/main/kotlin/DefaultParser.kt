import main.kotlin.lexer.Token
import main.kotlin.parser.ParseResult
import org.example.ast.ASTNode
import rules.MatchedRule
import rules.RuleMatcher

class DefaultParser(
    private val ruleMatcher: RuleMatcher,
) {
    
    fun parse(tokens: List<Token>): List<ASTNode> {
        val ast = mutableListOf<ASTNode>()
        var pos = 0

        while (pos < tokens.size) {
            when (val res = ruleMatcher.matchNext(tokens, pos)) {
                is ParseResult.Success<MatchedRule> -> {
                    val matched = res.node
                    val node = matched.rule.builder.buildNode(matched.matchedTokens)
                    ast.add(node)
                    pos = res.nextPosition
                }
                is ParseResult.Failure -> {
                    throw IllegalArgumentException("Syntax error at $pos: ${res.message}")
                }
            }
        }
        return ast.toList()
    }
}

   /* private fun parseRecursive(tokens: List<Token>, pos: Int): ParseResult<ASTNode>? {
        if (pos >= tokens.size) return null

        return when (val matchResult = ruleMatcher.matchNext(tokens, pos)) { //si nunca voy a tener mas opciones de ParseResult
            is ParseResult.Success<*> -> {
                val node = matchResult.node.rule.buildNode(matchResult.node.matchedTokens)
                val nextResult = parseRecursive(tokens, matchResult.nextPosition)
                ParseResult.Success(node, nextResult?.nextPosition ?: matchResult.nextPosition)
            }
            is ParseResult.Failure -> matchResult
        }
    }*/
