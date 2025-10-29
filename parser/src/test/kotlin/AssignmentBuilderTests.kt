package test.parserTest

import builders.AssignmentBuilder
import main.kotlin.lexer.*
import org.example.ast.*
import types.*
import kotlin.test.*

class AssignmentBuilderTests {
    private val assignmentBuilder = AssignmentBuilder()

    @Test
    fun should_build_simple_assignment_with_literal_number() {
        val tokens =
            listOf(
                Token(IdentifierType, "x", 1, 1),
                Token(AssignmentType, "=", 1, 3),
                Token(LiteralNumber, "42", 1, 5),
                Token(PunctuationType, ";", 1, 7),
            )

        val result = assignmentBuilder.buildNode(tokens)

        assertTrue(result is AssignmentNode)
        val assignment = result as AssignmentNode

        assertTrue(assignment.identifier is IdentifierNode)
        assertEquals("x", (assignment.identifier as IdentifierNode).name)

        assertTrue(assignment.value is LiteralNode)
        val literal = assignment.value as LiteralNode
        assertEquals("42", literal.value)
        assertEquals(LiteralNumber, literal.literalType)
    }

    @Test
    fun should_build_simple_assignment_with_literal_string() {
        val tokens =
            listOf(
                Token(IdentifierType, "message", 1, 1),
                Token(AssignmentType, "=", 1, 9),
                Token(LiteralString, "\"hello\"", 1, 11),
                Token(PunctuationType, ";", 1, 18),
            )

        val result = assignmentBuilder.buildNode(tokens)

        assertTrue(result is AssignmentNode)
        val assignment = result as AssignmentNode

        assertTrue(assignment.identifier is IdentifierNode)
        assertEquals("message", (assignment.identifier as IdentifierNode).name)

        assertTrue(assignment.value is LiteralNode)
        val literal = assignment.value as LiteralNode
        assertEquals("hello", literal.value) // Should be unquoted
        assertEquals(LiteralString, literal.literalType)
    }

    @Test
    fun should_build_simple_assignment_with_identifier() {
        val tokens =
            listOf(
                Token(IdentifierType, "y", 1, 1),
                Token(AssignmentType, "=", 1, 3),
                Token(IdentifierType, "x", 1, 5),
                Token(PunctuationType, ";", 1, 6),
            )

        val result = assignmentBuilder.buildNode(tokens)

        assertTrue(result is AssignmentNode)
        val assignment = result as AssignmentNode

        assertTrue(assignment.identifier is IdentifierNode)
        assertEquals("y", (assignment.identifier as IdentifierNode).name)

        assertTrue(assignment.value is IdentifierNode)
        val identifier = assignment.value as IdentifierNode
        assertEquals("x", identifier.name)
    }

    @Test
    fun should_build_binary_expression_assignment() {
        val tokens =
            listOf(
                Token(IdentifierType, "result", 1, 1),
                Token(AssignmentType, "=", 1, 8),
                Token(LiteralNumber, "5", 1, 10),
                Token(OperatorType, "+", 1, 12),
                Token(LiteralNumber, "3", 1, 14),
                Token(PunctuationType, ";", 1, 15),
            )

        val result = assignmentBuilder.buildNode(tokens)

        assertTrue(result is AssignmentNode)
        val assignment = result as AssignmentNode

        assertTrue(assignment.identifier is IdentifierNode)
        assertEquals("result", (assignment.identifier as IdentifierNode).name)

        assertTrue(assignment.value is BinaryOpNode)
        val binaryOp = assignment.value as BinaryOpNode
        assertEquals("+", binaryOp.operator)

        assertTrue(binaryOp.left is LiteralNode)
        assertEquals("5", (binaryOp.left as LiteralNode).value)

        assertTrue(binaryOp.right is LiteralNode)
        assertEquals("3", (binaryOp.right as LiteralNode).value)
    }

    @Test
    fun should_build_complex_binary_expression_assignment() {
        val tokens =
            listOf(
                Token(IdentifierType, "result", 1, 1),
                Token(AssignmentType, "=", 1, 8),
                Token(LiteralNumber, "5", 1, 10),
                Token(OperatorType, "+", 1, 12),
                Token(LiteralNumber, "3", 1, 14),
                Token(OperatorType, "*", 1, 16),
                Token(LiteralNumber, "2", 1, 18),
                Token(PunctuationType, ";", 1, 19),
            )

        val result = assignmentBuilder.buildNode(tokens)

        assertTrue(result is AssignmentNode)
        val assignment = result as AssignmentNode

        assertTrue(assignment.identifier is IdentifierNode)
        assertEquals("result", (assignment.identifier as IdentifierNode).name)

        assertTrue(assignment.value is BinaryOpNode)
        val binaryOp = assignment.value as BinaryOpNode
        assertEquals("*", binaryOp.operator) // Right-associative: (5 + 3) * 2

        assertTrue(binaryOp.left is BinaryOpNode)
        val leftBinary = binaryOp.left as BinaryOpNode
        assertEquals("+", leftBinary.operator)

        assertTrue(binaryOp.right is LiteralNode)
        assertEquals("2", (binaryOp.right as LiteralNode).value)
    }

    @Test
    fun should_build_assignment_with_mixed_operands() {
        val tokens =
            listOf(
                Token(IdentifierType, "result", 1, 1),
                Token(AssignmentType, "=", 1, 8),
                Token(IdentifierType, "x", 1, 10),
                Token(OperatorType, "+", 1, 12),
                Token(LiteralNumber, "5", 1, 14),
                Token(PunctuationType, ";", 1, 15),
            )

        val result = assignmentBuilder.buildNode(tokens)

        assertTrue(result is AssignmentNode)
        val assignment = result as AssignmentNode

        assertTrue(assignment.identifier is IdentifierNode)
        assertEquals("result", (assignment.identifier as IdentifierNode).name)

        assertTrue(assignment.value is BinaryOpNode)
        val binaryOp = assignment.value as BinaryOpNode
        assertEquals("+", binaryOp.operator)

        assertTrue(binaryOp.left is IdentifierNode)
        assertEquals("x", (binaryOp.left as IdentifierNode).name)

        assertTrue(binaryOp.right is LiteralNode)
        assertEquals("5", (binaryOp.right as LiteralNode).value)
    }

    @Test
    fun should_unquote_string_literals() {
        val tokens =
            listOf(
                Token(IdentifierType, "message", 1, 1),
                Token(AssignmentType, "=", 1, 9),
                Token(LiteralString, "\"hello world\"", 1, 11),
                Token(PunctuationType, ";", 1, 24),
            )

        val result = assignmentBuilder.buildNode(tokens)

        assertTrue(result is AssignmentNode)
        val assignment = result as AssignmentNode

        assertTrue(assignment.value is LiteralNode)
        val literal = assignment.value as LiteralNode
        assertEquals("hello world", literal.value) // Should be unquoted
    }

    @Test
    fun should_handle_string_without_quotes() {
        val tokens =
            listOf(
                Token(IdentifierType, "message", 1, 1),
                Token(AssignmentType, "=", 1, 9),
                Token(LiteralString, "hello", 1, 11), // No quotes
                Token(PunctuationType, ";", 1, 16),
            )

        val result = assignmentBuilder.buildNode(tokens)

        assertTrue(result is AssignmentNode)
        val assignment = result as AssignmentNode

        assertTrue(assignment.value is LiteralNode)
        val literal = assignment.value as LiteralNode
        assertEquals("hello", literal.value) // Should remain unchanged
    }

    @Test
    fun should_handle_empty_string() {
        val tokens =
            listOf(
                Token(IdentifierType, "message", 1, 1),
                Token(AssignmentType, "=", 1, 9),
                Token(LiteralString, "\"\"", 1, 11),
                Token(PunctuationType, ";", 1, 13),
            )

        val result = assignmentBuilder.buildNode(tokens)

        assertTrue(result is AssignmentNode)
        val assignment = result as AssignmentNode

        assertTrue(assignment.value is LiteralNode)
        val literal = assignment.value as LiteralNode
        assertEquals("", literal.value) // Should be empty string
    }
}
