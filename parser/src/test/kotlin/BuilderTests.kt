package test.parserTest

import builders.PrintBuilder
import main.kotlin.lexer.*
import org.example.LiteralNumber
import org.example.LiteralString
import org.example.ast.*
import types.OperatorType
import types.PunctuationType
import kotlin.test.*

class BuilderTests {
    @Test
    fun printBuilder_should_create_println_node_with_string_literal() {
        // Arrange - tokens que representan println("hello");
        val builder = PrintBuilder()
        val tokens =
            listOf(
                Token(IdentifierType, "println", 1, 1),
                Token(PunctuationType, "(", 1, 8),
                Token(LiteralString, "\"hello\"", 1, 9),
                Token(PunctuationType, ")", 1, 16),
                Token(PunctuationType, ";", 1, 17),
            )

        // Act
        val node = builder.buildNode(tokens)

        // Assert - verificamos la estructura del AST
        assertTrue(node is PrintlnNode)

        val printNode = node as PrintlnNode
        assertTrue(printNode.value is LiteralNode)

        val literal = printNode.value as LiteralNode
        assertEquals("hello", literal.value) // sin comillas
        assertEquals(LiteralString, literal.literalType)
    }

    @Test
    fun printBuilder_should_create_println_node_with_number_literal() {
        // Arrange
        val builder = PrintBuilder()
        val tokens =
            listOf(
                Token(IdentifierType, "println", 1, 1),
                Token(PunctuationType, "(", 1, 8),
                Token(LiteralNumber, "123", 1, 9),
                Token(PunctuationType, ")", 1, 12),
                Token(PunctuationType, ";", 1, 13),
            )

        // Act
        val node = builder.buildNode(tokens)

        // Assert
        assertTrue(node is PrintlnNode)

        val printNode = node as PrintlnNode
        assertTrue(printNode.value is LiteralNode)

        val literal = printNode.value as LiteralNode
        assertEquals("123", literal.value)
        assertEquals(LiteralNumber, literal.literalType)
    }

    @Test
    fun printBuilder_should_create_println_node_with_binary_expression() {
        // Arrange - tokens que representan println(12 + 8);
        val builder = PrintBuilder()
        val tokens =
            listOf(
                Token(IdentifierType, "println", 1, 1),
                Token(PunctuationType, "(", 1, 8),
                Token(LiteralNumber, "12", 1, 9),
                Token(OperatorType, "+", 1, 12),
                Token(LiteralNumber, "8", 1, 14),
                Token(PunctuationType, ")", 1, 15),
                Token(PunctuationType, ";", 1, 16),
            )

        // Act
        val node = builder.buildNode(tokens)

        // Assert
        assertTrue(node is PrintlnNode)

        val printNode = node as PrintlnNode
        assertTrue(printNode.value is BinaryOpNode)

        val binaryOp = printNode.value as BinaryOpNode
        assertEquals("+", binaryOp.operator)

        // Verificar operandos
        assertTrue(binaryOp.left is LiteralNode)
        assertTrue(binaryOp.right is LiteralNode)

        val leftLiteral = binaryOp.left as LiteralNode
        val rightLiteral = binaryOp.right as LiteralNode

        assertEquals("12", leftLiteral.value)
        assertEquals("8", rightLiteral.value)
    }

    @Test
    fun printBuilder_should_create_println_node_with_identifier() {
        // Arrange - tokens que representan println(variable);
        val builder = PrintBuilder()
        val tokens =
            listOf(
                Token(IdentifierType, "println", 1, 1),
                Token(PunctuationType, "(", 1, 8),
                Token(IdentifierType, "variable", 1, 9),
                Token(PunctuationType, ")", 1, 17),
                Token(PunctuationType, ";", 1, 18),
            )

        // Act
        val node = builder.buildNode(tokens)

        // Assert
        assertTrue(node is PrintlnNode)

        val printNode = node as PrintlnNode
        assertTrue(printNode.value is IdentifierNode)

        val identifier = printNode.value as IdentifierNode
        assertEquals("variable", identifier.name)
    }
}
