
import org.Parser
import org.example.Lexer.Location
import org.example.Lexer.Token
import org.example.astnode.ASTNode
import org.example.astnode.astNodeVisitor.ASTNodeVisitor
import org.example.astnode.astNodeVisitor.VisitorResult
import org.example.iterator.PrintScriptIterator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import semanticAnalysis.SemanticAnalyzer

private class DummyNode(override val type: String, override val location: Location) : ASTNode {
    override fun accept(visitor: ASTNodeVisitor): VisitorResult = VisitorResult.Empty
}

private class TestBuilder(
    override val formula: String,
) : ASTNodeBuilder {
    override fun generate(tokens: List<Token>, parser: Parser): ASTNode =
        DummyNode("FromBuilder", tokens.first().location)

    override fun checkFormula(tokensString: String): Boolean = tokensString == formula
}

private fun emptyParser(): Parser =
    Parser(
        astGenerator = ASTGenerator(emptyList()),
        semanticAnalyzer = SemanticAnalyzer(emptyList()),
        supportedStructures = emptyList(),
        tokenIterator =
            object : PrintScriptIterator<Token> {
                override fun hasNext(): Boolean = false
                override fun next(): Token? = null
                override fun peek(): Token? = null
            },
    )

class ASTGeneratorTest {

    private fun t(type: String, value: String = "", line: Int = 1, col: Int = 1) =
        Token(type, value, Location(line, col))

    @Test
    fun `generator selects the first accepting builder`() {
        val builder1 = TestBuilder("A B SemicolonToken")
        val builder2 = TestBuilder("X Y SemicolonToken")
        val gen = ASTGenerator(listOf(builder1, builder2))
        val buffer = arrayListOf(t("A"), t("B"), t("SemicolonToken", ";"))
        val node = gen.generate(buffer, emptyParser())
        assertEquals("FromBuilder", node.type)
    }

    @Test
    fun `generator throws when no builder matches`() {
        val gen = ASTGenerator(emptyList())
        val buffer = arrayListOf(t("Z"), t("W"), t("SemicolonToken", ";"))
        assertThrows(Exception::class.java) {
            gen.generate(buffer, emptyParser())
        }
    }
}
