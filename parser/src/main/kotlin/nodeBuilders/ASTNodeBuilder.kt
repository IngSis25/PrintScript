import org.Parser
import org.example.Lexer.Token
import org.example.astnode.ASTNode

interface ASTNodeBuilder {
    val formula: String

    fun generate(
        tokens: List<Token>,
        parser: Parser,
    ): ASTNode

    fun checkFormula(tokensString: String): Boolean
}
