package nodeBuilders

import ASTNodeBuilder
import org.Parser
import org.example.Lexer.Token
import org.example.astnode.ASTNode
import org.example.astnode.PatternFactory
import org.example.astnode.expressionNodes.ExpressionNode
import org.example.astnode.expressionNodes.IdentifierNode
import org.example.astnode.statamentNode.AssignmentNode

class AssignmentNodeBuilder : ASTNodeBuilder {
    override val formula: String = "IdentifierToken AssignationToken ExpressionNode SemicolonToken"

    override fun generate(
        tokens: List<Token>,
        parser: Parser,
    ): ASTNode =
        AssignmentNode(
            type = "AssignmentNode",
            location = tokens[0].location,
            value =
                ExpressionNodeBuilder()
                    .generate(tokens.subList(2, tokens.size - 1), parser) as ExpressionNode,
            identifier =
                IdentifierNodeBuilder()
                    .generate(tokens.subList(0, 1), parser) as IdentifierNode,
        )

    override fun checkFormula(tokensString: String): Boolean {
        val expressionPattern = PatternFactory.getExpressionPattern()
        val pattern =
            "^IdentifierToken\\s*AssignationToken\\s*" +
                "$expressionPattern\\s*SemicolonToken$"
        return Regex(pattern).matches(tokensString)
    }
}
