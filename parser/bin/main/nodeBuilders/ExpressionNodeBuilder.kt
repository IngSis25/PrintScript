package nodeBuilders

import ASTNodeBuilder
import nodeBuilders.expressions.ExpressionsNodeBuilderFactory
import org.Parser
import org.example.Lexer.Token
import org.example.astnode.ASTNode

class ExpressionNodeBuilder(
    private val expressionsBuilders: List<ASTNodeBuilder> =
        ExpressionsNodeBuilderFactory()
            .createV11(),
) : ASTNodeBuilder {
    override val formula: String = ""

    override fun generate(
        tokens: List<Token>,
        parser: Parser,
    ): ASTNode {
        for (builder in expressionsBuilders) {
            if (builder.checkFormula(tokens.joinToString(" ") { it.type })) {
                return builder.generate(tokens, parser)
            }
        }
        throw IllegalArgumentException("Invalid expression generation at ${tokens[0].location}")
    }

    override fun checkFormula(tokensString: String): Boolean {
        for (builder in expressionsBuilders) {
            if (builder.checkFormula(tokensString)) {
                return true
            }
        }
        return false
    }
}
