package test.parserTest

import builders.ExpressionBuilder
import main.kotlin.lexer.*
import org.example.LiteralNumber
import org.example.ast.*
import types.OperatorType
import kotlin.test.*

class ExpressionBuilderTests {
    @Test
    fun expressionBuilder_should_create_binary_op_node_with_addition() {
        // Arrange - tokens que representan 12 + 8
        val builder = ExpressionBuilder()
        val tokens =
            listOf(
                Token(LiteralNumber, "12", 1, 1),
                Token(OperatorType, "+", 1, 4),
                Token(LiteralNumber, "8", 1, 6),
            )

        // Act
        val node = builder.buildNode(tokens)

        // Assert
        assertTrue(node is BinaryOpNode)

        val binaryOp = node as BinaryOpNode
        assertEquals("+", binaryOp.operator)

        // Verificar operandos
        assertTrue(binaryOp.left is LiteralNode)
        assertTrue(binaryOp.right is LiteralNode)

        val leftLiteral = binaryOp.left as LiteralNode
        val rightLiteral = binaryOp.right as LiteralNode

        assertEquals("12", leftLiteral.value)
        assertEquals(LiteralNumber, leftLiteral.literalType)
        assertEquals("8", rightLiteral.value)
        assertEquals(LiteralNumber, rightLiteral.literalType)
    }

    @Test
    fun expressionBuilder_should_create_binary_op_node_with_subtraction() {
        // Arrange - tokens que representan 20 - 5
        val builder = ExpressionBuilder()
        val tokens =
            listOf(
                Token(LiteralNumber, "20", 1, 1),
                Token(OperatorType, "-", 1, 4),
                Token(LiteralNumber, "5", 1, 6),
            )

        // Act
        val node = builder.buildNode(tokens)

        // Assert
        assertTrue(node is BinaryOpNode)

        val binaryOp = node as BinaryOpNode
        assertEquals("-", binaryOp.operator)

        val leftLiteral = binaryOp.left as LiteralNode
        val rightLiteral = binaryOp.right as LiteralNode

        assertEquals("20", leftLiteral.value)
        assertEquals("5", rightLiteral.value)
    }

    @Test
    fun expressionBuilder_should_create_binary_op_node_with_multiplication() {
        // Arrange - tokens que representan 3 * 4
        val builder = ExpressionBuilder()
        val tokens =
            listOf(
                Token(LiteralNumber, "3", 1, 1),
                Token(OperatorType, "*", 1, 3),
                Token(LiteralNumber, "4", 1, 5),
            )

        // Act
        val node = builder.buildNode(tokens)

        // Assert
        assertTrue(node is BinaryOpNode)

        val binaryOp = node as BinaryOpNode
        assertEquals("*", binaryOp.operator)

        val leftLiteral = binaryOp.left as LiteralNode
        val rightLiteral = binaryOp.right as LiteralNode

        assertEquals("3", leftLiteral.value)
        assertEquals("4", rightLiteral.value)
    }
}
