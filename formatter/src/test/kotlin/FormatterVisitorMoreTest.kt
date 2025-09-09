package formatter

import config.FormatterConfig
import org.example.ast.ASTNode
import org.example.ast.IdentifierNode
import org.example.ast.LiteralNode
import org.example.ast.VariableDeclarationNode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FormatterVisitorMoreTest {
    private fun config(): FormatterConfig =
        FormatterConfig(
            lineBreaksBeforePrints = 0,
            spaceAroundEquals = true,
            spaceBeforeColon = false,
            spaceAfterColon = false,
        )

    @Test
    fun `variable declaration with value but no type`() {
        // Arrange
        val node =
            VariableDeclarationNode(
                identifier = IdentifierNode("y"),
                varType = null,
                value = LiteralNode("7", literalType = org.example.LiteralNumber),
            )
        val sb = StringBuilder()
        val visitor = FormatterVisitor(config(), sb)
        // Act
        visitor.evaluate(node)
        // Assert
        assertEquals("let y = 7;\n", sb.toString())
    }

    @Test
    fun `variable declaration with type but no value`() {
        val node =
            VariableDeclarationNode(
                identifier = IdentifierNode("z"),
                varType = "STRING",
                value = null,
            )
        val sb = StringBuilder()
        val visitor = FormatterVisitor(config(), sb)
        visitor.evaluate(node)
        assertEquals("let z:STRING;\n", sb.toString())
    }

    @Test
    fun `identifier alone is formatted without terminator`() {
        val node = IdentifierNode("onlyId")
        val sb = StringBuilder()
        val visitor = FormatterVisitor(config(), sb)
        visitor.evaluate(node)
        assertEquals("onlyId", sb.toString())
    }

    @Test
    fun `unsupported node throws`() {
        // Arrange
        class UnknownNode : ASTNode
        val node = UnknownNode()
        val sb = StringBuilder()
        val visitor = FormatterVisitor(config(), sb)
        // Act + Assert
        assertFailsWith<IllegalArgumentException> {
            visitor.evaluate(node)
        }
    }
}
