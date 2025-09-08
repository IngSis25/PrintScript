package org.example

import main.kotlin.analyzer.SymbolTable
import main.kotlin.analyzer.TypeOf
import main.kotlin.analyzer.Types
import org.example.LiteralNumber
import org.example.LiteralString
import org.example.ast.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TypeOfTest {
    @Test
    fun `should infer literal types correctly`() {
        val numberLiteral = LiteralNode("42", LiteralNumber)
        val stringLiteral = LiteralNode("hello", LiteralString)

        assertEquals(Types.NUMBER, TypeOf.literal(numberLiteral))
        assertEquals(Types.STRING, TypeOf.literal(stringLiteral))
    }

    @Test
    fun `should infer identifier types from symbol table`() {
        val symbolTable =
            SymbolTable()
                .declare("x", Types.NUMBER)
                .declare("y", Types.STRING)

        val xIdentifier = IdentifierNode("x")
        val yIdentifier = IdentifierNode("y")
        val zIdentifier = IdentifierNode("z")

        assertEquals(Types.NUMBER, TypeOf.identifier(xIdentifier, symbolTable))
        assertEquals(Types.STRING, TypeOf.identifier(yIdentifier, symbolTable))
        assertEquals(Types.UNKNOWN, TypeOf.identifier(zIdentifier, symbolTable))
    }

    @Test
    fun `should infer binary operation types correctly`() {
        val symbolTable =
            SymbolTable()
                .declare("x", Types.NUMBER)
                .declare("y", Types.STRING)

        // Number + Number = Number
        val numberOp =
            BinaryOpNode(
                left = LiteralNode("5", LiteralNumber),
                operator = "+",
                right = LiteralNode("3", LiteralNumber),
            )
        assertEquals(Types.NUMBER, TypeOf.binaryOperation(numberOp, symbolTable))

        // String + String = String
        val stringOp =
            BinaryOpNode(
                left = LiteralNode("hello", LiteralString),
                operator = "+",
                right = LiteralNode("world", LiteralString),
            )
        assertEquals(Types.STRING, TypeOf.binaryOperation(stringOp, symbolTable))

        // Number - Number = Number
        val subtractOp =
            BinaryOpNode(
                left = LiteralNode("5", LiteralNumber),
                operator = "-",
                right = LiteralNode("3", LiteralNumber),
            )
        assertEquals(Types.NUMBER, TypeOf.binaryOperation(subtractOp, symbolTable))
    }

    @Test
    fun `should detect incompatible operations`() {
        val symbolTable = SymbolTable()

        // String - String = Unknown (invalid)
        val invalidOp =
            BinaryOpNode(
                left = LiteralNode("hello", LiteralString),
                operator = "-",
                right = LiteralNode("world", LiteralString),
            )
        assertEquals(Types.UNKNOWN, TypeOf.binaryOperation(invalidOp, symbolTable))

        // Number + String = Unknown (invalid)
        val mixedOp =
            BinaryOpNode(
                left = LiteralNode("5", LiteralNumber),
                operator = "+",
                right = LiteralNode("hello", LiteralString),
            )
        assertEquals(Types.UNKNOWN, TypeOf.binaryOperation(mixedOp, symbolTable))
    }

    @Test
    fun `should check operation compatibility`() {
        assertTrue(TypeOf.canPerformOperation("+", Types.NUMBER, Types.NUMBER))
        assertTrue(TypeOf.canPerformOperation("+", Types.STRING, Types.STRING))
        assertFalse(TypeOf.canPerformOperation("+", Types.NUMBER, Types.STRING))

        assertTrue(TypeOf.canPerformOperation("-", Types.NUMBER, Types.NUMBER))
        assertFalse(TypeOf.canPerformOperation("-", Types.STRING, Types.STRING))

        assertTrue(TypeOf.canPerformOperation("==", Types.NUMBER, Types.NUMBER))
        assertTrue(TypeOf.canPerformOperation("==", Types.STRING, Types.STRING))
        assertFalse(TypeOf.canPerformOperation("==", Types.NUMBER, Types.STRING))
    }

    @Test
    fun `should check type compatibility`() {
        assertTrue(TypeOf.isCompatible(Types.NUMBER, Types.NUMBER))
        assertTrue(TypeOf.isCompatible(Types.STRING, Types.STRING))
        assertFalse(TypeOf.isCompatible(Types.NUMBER, Types.STRING))

        // Unknown types are compatible with anything
        assertTrue(TypeOf.isCompatible(Types.UNKNOWN, Types.NUMBER))
        assertTrue(TypeOf.isCompatible(Types.NUMBER, Types.UNKNOWN))
    }

    @Test
    fun `should check if expression is simple`() {
        val identifier = IdentifierNode("x")
        val literal = LiteralNode("42", LiteralNumber)
        val binaryOp =
            BinaryOpNode(
                left = identifier,
                operator = "+",
                right = literal,
            )

        assertTrue(TypeOf.isSimpleExpression(identifier))
        assertTrue(TypeOf.isSimpleExpression(literal))
        assertFalse(TypeOf.isSimpleExpression(binaryOp))
    }

    @Test
    fun `should check if expression is complex`() {
        val identifier = IdentifierNode("x")
        val literal = LiteralNode("42", LiteralNumber)
        val binaryOp =
            BinaryOpNode(
                left = identifier,
                operator = "+",
                right = literal,
            )

        assertFalse(TypeOf.isComplexExpression(identifier))
        assertFalse(TypeOf.isComplexExpression(literal))
        assertTrue(TypeOf.isComplexExpression(binaryOp))
    }

    @Test
    fun `should handle boolean operations`() {
        val symbolTable = SymbolTable()

        // Boolean && Boolean = Boolean
        val booleanAnd =
            BinaryOpNode(
                left = LiteralNode("true", LiteralString), // Assuming this represents boolean
                operator = "&&",
                right = LiteralNode("false", LiteralString),
            )
        assertEquals(Types.UNKNOWN, TypeOf.binaryOperation(booleanAnd, symbolTable))

        // Boolean || Boolean = Boolean
        val booleanOr =
            BinaryOpNode(
                left = LiteralNode("true", LiteralString),
                operator = "||",
                right = LiteralNode("false", LiteralString),
            )
        assertEquals(Types.UNKNOWN, TypeOf.binaryOperation(booleanOr, symbolTable))
    }

    @Test
    fun `should handle comparison operations`() {
        val symbolTable = SymbolTable()

        // Number == Number = Boolean
        val numberComparison =
            BinaryOpNode(
                left = LiteralNode("5", LiteralNumber),
                operator = "==",
                right = LiteralNode("5", LiteralNumber),
            )
        assertEquals(Types.BOOLEAN, TypeOf.binaryOperation(numberComparison, symbolTable))

        // String != String = Boolean
        val stringComparison =
            BinaryOpNode(
                left = LiteralNode("hello", LiteralString),
                operator = "!=",
                right = LiteralNode("world", LiteralString),
            )
        assertEquals(Types.BOOLEAN, TypeOf.binaryOperation(stringComparison, symbolTable))

        // Number < Number = Boolean
        val lessThan =
            BinaryOpNode(
                left = LiteralNode("3", LiteralNumber),
                operator = "<",
                right = LiteralNode("5", LiteralNumber),
            )
        assertEquals(Types.BOOLEAN, TypeOf.binaryOperation(lessThan, symbolTable))
    }

    @Test
    fun `should handle unknown operations`() {
        val symbolTable = SymbolTable()

        val unknownOp =
            BinaryOpNode(
                left = LiteralNode("5", LiteralNumber),
                operator = "??",
                right = LiteralNode("3", LiteralNumber),
            )
        assertEquals(Types.UNKNOWN, TypeOf.binaryOperation(unknownOp, symbolTable))
    }

    @Test
    fun `should handle unknown node types`() {
        val symbolTable = SymbolTable()

        // Create a mock node that's not handled
        val unknownNode = object : ASTNode {}

        assertEquals(Types.UNKNOWN, TypeOf.inferType(unknownNode, symbolTable))
    }
}
