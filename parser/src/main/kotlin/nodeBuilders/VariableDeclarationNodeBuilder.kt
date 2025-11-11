package nodeBuilders

import ASTNodeBuilder
import org.Parser
import org.example.Lexer.Token
import org.example.astnode.ASTNode
import org.example.astnode.PatternFactory
import org.example.astnode.expressionNodes.ExpressionNode
import org.example.astnode.expressionNodes.IdentifierNode
import org.example.astnode.expressionNodes.LiteralNode
import org.example.astnode.expressionNodes.LiteralValue
import org.example.astnode.statamentNode.VariableDeclarationNode

class VariableDeclarationNodeBuilder : ASTNodeBuilder {
    override val formula: String =
        "DeclarationToken IdentifierToken ColonToken TypeToken " +
            "AssignationToken ExpressionNode SemicolonToken"

    override fun generate(
        tokens: List<Token>,
        parser: Parser,
    ): ASTNode {
        if (tokens.size == 5) {
            return VariableDeclarationNode(
                type = "VariableDeclarationNode",
                location = tokens[0].location,
                identifier =
                    IdentifierNodeBuilder()
                        .generate(tokens.subList(0, 4), parser) as IdentifierNode,
                init =
                    LiteralNode(
                        type = "LiteralNode",
                        location = tokens[4].location,
                        value = LiteralValue.NullValue,
                    ) as ExpressionNode,
                kind = tokens[0].value,
            )
        }

        return VariableDeclarationNode(
            type = "VariableDeclarationNode",
            location = tokens[0].location,
            identifier =
                IdentifierNodeBuilder()
                    .generate(tokens.subList(0, 4), parser) as IdentifierNode,
            init =
                ExpressionNodeBuilder()
                    .generate(tokens.subList(5, tokens.size - 1), parser) as ExpressionNode,
            kind = tokens[0].value,
        )
    }

    override fun checkFormula(tokensString: String): Boolean {
        val expressionPattern = PatternFactory.getExpressionPattern()
        val pattern =
            "^DeclarationToken\\s*IdentifierToken\\s*ColonToken" +
                "\\s*TypeToken(\\s*AssignationToken\\s*$expressionPattern)?\\s*SemicolonToken$"
        return Regex(pattern).matches(tokensString)
    }
}
