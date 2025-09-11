package formatter

import org.example.LiteralString
import org.example.ast.AssignmentNode
import org.example.ast.BinaryOpNode
import org.example.ast.IdentifierNode
import org.example.ast.LiteralNode
import org.example.ast.PrintlnNode
import org.example.ast.VariableDeclarationNode
import org.example.formatter.FormatterVisitor
import org.example.formatter.config.FormatterConfig
import kotlin.test.Test
import kotlin.test.assertEquals

class FormatterVisitorTest {
    // AAA: Arrange, Act, Assert

    private fun defaultConfig(): FormatterConfig =
        FormatterConfig(
            lineBreaksBeforePrints = 1,
            spaceAroundEquals = true,
            spaceBeforeColon = false,
            spaceAfterColon = true,
        )

    @Test
    fun `formats variable declaration with type and value`() {
        // Arrange
        val node =
            VariableDeclarationNode(
                identifier = IdentifierNode("x"),
                varType = "NUMBER",
                value = LiteralNode("5", literalType = org.example.LiteralNumber),
            )
        val sb = StringBuilder()
        val visitor = FormatterVisitor(defaultConfig(), sb)
        // Act
        visitor.evaluate(node)
        // Assert
        assertEquals("let x: NUMBER = 5;\n", sb.toString())
    }

    @Test
    fun `formats assignment`() {
        val node =
            AssignmentNode(
                identifier = IdentifierNode("x"),
                value = LiteralNode("10", literalType = org.example.LiteralNumber),
            )
        val sb = StringBuilder()
        val visitor = FormatterVisitor(defaultConfig(), sb)
        visitor.evaluate(node)
        assertEquals("x = 10;\n", sb.toString())
    }

    @Test
    fun `formats println with string adds line break before`() {
        val node = PrintlnNode(LiteralNode("hi", LiteralString))
        val sb = StringBuilder()
        val visitor = FormatterVisitor(defaultConfig(), sb)
        visitor.evaluate(node)
        assertEquals("\nprintln(\"hi\");\n", sb.toString())
    }

    @Test
    fun `formats binary expression with parentheses when nested`() {
        // Arrange: (x + 1) * 2 -> inner binary should be parenthesized
        val inner =
            BinaryOpNode(
                left = IdentifierNode("x"),
                operator = "+",
                right = LiteralNode("1", literalType = org.example.LiteralNumber),
            )
        val root =
            BinaryOpNode(
                left = inner,
                operator = "*",
                right = LiteralNode("2", literalType = org.example.LiteralNumber),
            )
        val sb = StringBuilder()
        val visitor = FormatterVisitor(defaultConfig(), sb)
        // Act
        visitor.evaluate(root)
        // Assert
        assertEquals("(x + 1) * 2", sb.toString())
    }
}
