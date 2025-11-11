import org.Parser
import org.example.Lexer.Token
import org.example.astnode.ASTNode
import kotlin.collections.joinToString

open class ASTGenerator(
    private val builders: List<ASTNodeBuilder>,
) {
    fun generate(
        buffer: ArrayList<Token>,
        parser: Parser,
    ): ASTNode {
        for (builder in builders) {
            if (builder.checkFormula(getFormula(buffer))) {
                return builder.generate(buffer, parser)
            }
        }

        throw Exception(
            "Invalid statement at ${buffer.first().location}." +
                "No formula matches the tokens: ${buffer.joinToString(" ") { it.type }}",
        )
    }

    private fun getFormula(buffer: ArrayList<Token>): String = buffer.joinToString(" ") { it.type }
}
