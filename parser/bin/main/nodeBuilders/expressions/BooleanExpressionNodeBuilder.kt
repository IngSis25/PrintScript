package nodeBuilders.expressions

import ASTNodeBuilder
import nodeBuilders.IdentifierNodeBuilder
import org.Parser
import org.example.Lexer.Token
import org.example.astnode.ASTNode
import org.example.astnode.PatternFactory
import org.example.astnode.expressionNodes.LiteralNode
import org.example.astnode.expressionNodes.LiteralValue

class BooleanExpressionNodeBuilder : ASTNodeBuilder {
    override val formula = PatternFactory.getBooleanExpressionPattern()

    override fun generate(
        tokens: List<Token>,
        parser: Parser,
    ): ASTNode {
        if (tokens.size != 1) {
            throw IllegalArgumentException(
                "Not supporting boolean expressions with more than one token",
            )
        }

        return when (tokens[0].type) {
            "BooleanToken" -> {
                val value = tokens[0].value.toBoolean()
                LiteralNode(
                    type = "LiteralNode",
                    location = tokens[0].location,
                    value = LiteralValue.BooleanValue(value),
                )
            }
            "IdentifierToken" -> IdentifierNodeBuilder().generate(tokens, parser)
            else -> throw IllegalArgumentException("Unexpected token type ${tokens[0].type}")
        }
    }

    override fun checkFormula(tokensString: String): Boolean = Regex(formula).matches(tokensString)
}
