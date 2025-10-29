package test.parserTest

import builders.PrintBuilder
import builders.VariableDeclarationBuilder
import main.kotlin.lexer.*
import main.kotlin.parser.DefaultParser
import org.example.ast.*
import rules.PrintlnRule
import rules.RuleMatcher
import rules.VariableDeclarationRule
import types.*
import kotlin.test.*

class ImprovedParserTest {
    @Test
    fun parse_complex_expression_declaration() {
        // Arrange - let numberResult: number = 5 * 5 - 8;
        val tokens =
            listOf(
                Token(ModifierType, "let", 1, 1),
                Token(IdentifierType, "numberResult", 1, 5),
                Token(PunctuationType, ":", 1, 17),
                Token(NumberType, "number", 1, 19),
                Token(AssignmentType, "=", 1, 26),
                Token(LiteralNumber, "5", 1, 28),
                Token(OperatorType, "*", 1, 30),
                Token(LiteralNumber, "5", 1, 32),
                Token(OperatorType, "-", 1, 34),
                Token(LiteralNumber, "8", 1, 36),
                Token(PunctuationType, ";", 1, 37),
            )

        val ruleMatcher =
            RuleMatcher(
                listOf(
                    PrintlnRule(PrintBuilder()),
                    VariableDeclarationRule(VariableDeclarationBuilder()),
                ),
            )
        val parser = DefaultParser(ruleMatcher)

        // Act
        val ast = parser.parse(tokens)

        // Assert
        assertEquals(1, ast.size)
        assertTrue(ast[0] is VariableDeclarationNode)

        val varDecl = ast[0] as VariableDeclarationNode
        assertEquals("numberResult", (varDecl.identifier as IdentifierNode).name)
        assertEquals("number", varDecl.varType)
        assertTrue(varDecl.value is BinaryOpNode)

        val binaryOp = varDecl.value as BinaryOpNode
        assertEquals("-", binaryOp.operator)

        // Verificar que el lado izquierdo es otra expresión binaria (5 * 5)
        assertTrue(binaryOp.left is BinaryOpNode)
        val leftExpr = binaryOp.left as BinaryOpNode
        assertEquals("*", leftExpr.operator)
        assertEquals("5", (leftExpr.left as LiteralNode).value)
        assertEquals("5", (leftExpr.right as LiteralNode).value)

        // Verificar que el lado derecho es 8
        assertTrue(binaryOp.right is LiteralNode)
        assertEquals("8", (binaryOp.right as LiteralNode).value)
    }

    @Test
    fun parse_simple_declaration_without_type() {
        // Arrange - let result = 42;
        val tokens =
            listOf(
                Token(ModifierType, "let", 1, 1),
                Token(IdentifierType, "result", 1, 5),
                Token(AssignmentType, "=", 1, 12),
                Token(LiteralNumber, "42", 1, 14),
                Token(PunctuationType, ";", 1, 16),
            )

        val ruleMatcher =
            RuleMatcher(
                listOf(
                    VariableDeclarationRule(VariableDeclarationBuilder()),
                ),
            )
        val parser = DefaultParser(ruleMatcher)

        // Act
        val ast = parser.parse(tokens)

        // Assert
        assertEquals(1, ast.size)
        assertTrue(ast[0] is VariableDeclarationNode)

        val varDecl = ast[0] as VariableDeclarationNode
        assertEquals("result", (varDecl.identifier as IdentifierNode).name)
        assertEquals("number", varDecl.varType) // inferido del literal
        assertTrue(varDecl.value is LiteralNode)
        assertEquals("42", (varDecl.value as LiteralNode).value)
    }

    @Test
    fun parse_string_declaration() {
        // Arrange - let message: string = "hello";
        val tokens =
            listOf(
                Token(ModifierType, "let", 1, 1),
                Token(IdentifierType, "message", 1, 5),
                Token(PunctuationType, ":", 1, 12),
                Token(StringType, "string", 1, 14),
                Token(AssignmentType, "=", 1, 21),
                Token(LiteralString, "\"hello\"", 1, 23),
                Token(PunctuationType, ";", 1, 30),
            )

        val ruleMatcher =
            RuleMatcher(
                listOf(
                    VariableDeclarationRule(VariableDeclarationBuilder()),
                ),
            )
        val parser = DefaultParser(ruleMatcher)

        // Act
        val ast = parser.parse(tokens)

        // Assert
        assertEquals(1, ast.size)
        assertTrue(ast[0] is VariableDeclarationNode)

        val varDecl = ast[0] as VariableDeclarationNode
        assertEquals("message", (varDecl.identifier as IdentifierNode).name)
        assertEquals("string", varDecl.varType)
        assertTrue(varDecl.value is LiteralNode)
        assertEquals("hello", (varDecl.value as LiteralNode).value)
    }
}
