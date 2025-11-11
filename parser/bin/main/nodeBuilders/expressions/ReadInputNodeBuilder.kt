package nodeBuilders.expressions

import ASTNodeBuilder
import nodeBuilders.ExpressionNodeBuilder
import org.Parser
import org.example.Lexer.Token
import org.example.astnode.ASTNode
import org.example.astnode.PatternFactory
import org.example.astnode.expressionNodes.ExpressionNode
import org.example.astnode.expressionNodes.ReadInputNode

class ReadInputNodeBuilder : ASTNodeBuilder {
    override val formula = PatternFactory.getReadInputPattern()

    override fun generate(
        tokens: List<Token>,
        parser: Parser,
    ): ASTNode {
        if (tokens.isEmpty()) throw IllegalArgumentException("Empty token list")

        return ReadInputNode(
            type = "ReadInput",
            location = tokens[0].location,
            message =
                ExpressionNodeBuilder()
                    .generate(tokens.subList(2, tokens.size - 1), parser) as ExpressionNode,
        )
    }

    override fun checkFormula(tokensString: String): Boolean = Regex(formula).matches(tokensString)
}
