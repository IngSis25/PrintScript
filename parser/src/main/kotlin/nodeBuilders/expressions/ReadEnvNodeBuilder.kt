package nodeBuilders.expressions

import ASTNodeBuilder
import org.Parser
import org.example.Lexer.Token
import org.example.astnode.ASTNode
import org.example.astnode.PatternFactory
import org.example.astnode.expressionNodes.ReadEnvNode

class ReadEnvNodeBuilder : ASTNodeBuilder {
    override val formula = PatternFactory.getReadEnvironmentPattern()

    override fun generate(
        tokens: List<Token>,
        parser: Parser,
    ): ASTNode {
        if (tokens.isEmpty()) throw IllegalArgumentException("Empty token list")

        return ReadEnvNode(
            type = "ReadEnv",
            location = tokens[0].location,
            variableName = tokens[2].value,
        )
    }

    override fun checkFormula(tokensString: String): Boolean = Regex(formula).matches(tokensString)
}
