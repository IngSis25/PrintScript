package org.example

import main.kotlin.analyzer.SymbolTable
import main.kotlin.analyzer.Types
import org.example.ast.BinaryOpNode
import org.example.ast.IdentifierNode
import org.example.ast.LiteralNode
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
}
