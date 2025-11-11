package nodeBuilders

import ASTNodeBuilder
import org.Parser
import org.example.Lexer.Token
import org.example.astnode.ASTNode
import org.example.astnode.PatternFactory
import org.example.astnode.expressionNodes.ExpressionNode
import org.example.astnode.statamentNode.PrintStatementNode

class PrintNodeBuilder : ASTNodeBuilder {
    override val formula: String =
        "PrintToken OpenParenthesisToken ExpressionNode " +
            "CloseParenthesisToken SemicolonToken"

    override fun generate(
        tokens: List<Token>,
        parser: Parser,
    ): ASTNode =
        PrintStatementNode(
            type = "PrintStatementNode",
            location = tokens[0].location,
            value =
                ExpressionNodeBuilder()
                    .generate(tokens.subList(2, tokens.size - 2), parser) as ExpressionNode,
        )

    override fun checkFormula(tokensString: String): Boolean {
        val expressionPattern = PatternFactory.getExpressionPattern()
        val pattern =
            "^PrintToken\\s*OpenParenthesisToken\\s*$expressionPattern" +
                "\\s*CloseParenthesisToken\\s*SemicolonToken$"
        return Regex(pattern).matches(tokensString)
    }
}
