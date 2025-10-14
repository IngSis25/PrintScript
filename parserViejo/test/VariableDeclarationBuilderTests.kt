package test.parserTest

import builders.VariableDeclarationBuilder
import main.kotlin.lexer.*
import org.example.LiteralNumber
import org.example.LiteralString
import org.example.ast.*
import types.AssignmentType
import types.IdentifierType
import types.ModifierType
import types.NumberType
import types.PunctuationType
import types.StringType
import kotlin.test.*

class VariableDeclarationBuilderTests {
    private val builder = VariableDeclarationBuilder()

    @Test
    fun variableDeclarationBuilder_should_create_variable_declaration_node() {
        // Arrange - tokens que representan let nombre = "Juan";
        val tokens =
            listOf(
                Token(ModifierType, "let", 1, 1),
                Token(IdentifierType, "nombre", 1, 5),
                Token(AssignmentType, "=", 1, 12),
                Token(LiteralString, "\"Juan\"", 1, 14),
                Token(PunctuationType, ";", 1, 20),
            )

        // Act
        val node = builder.buildNode(tokens)

        // Assert
        assertTrue(node is VariableDeclarationNode)

        val varDecl = node

        // Verificar identifier
        assertTrue(true)
        val identifier = varDecl.identifier
        assertEquals("nombre", identifier.name)

        // Verificar tipo
        assertEquals("string", varDecl.varType)

        // Verificar valor
        assertTrue(varDecl.value is LiteralNode)
        val literal = varDecl.value as LiteralNode
        assertEquals("Juan", literal.value) // sin comillas
        assertEquals(LiteralString, literal.literalType)
    }

    @Test
    fun variableDeclarationBuilder_should_create_variable_with_different_name() {
        // Arrange - tokens que representan let edad = "25";
        val builder = VariableDeclarationBuilder()
        val tokens =
            listOf(
                Token(ModifierType, "let", 1, 1),
                Token(IdentifierType, "edad", 1, 5),
                Token(AssignmentType, "=", 1, 10),
                Token(LiteralString, "\"25\"", 1, 12),
                Token(PunctuationType, ";", 1, 16),
            )

        // Act
        val node = builder.buildNode(tokens)

        // Assert
        assertTrue(node is VariableDeclarationNode)

        val varDecl = node
        val identifier = varDecl.identifier
        val literal = varDecl.value as LiteralNode

        assertEquals("edad", identifier.name)
        assertEquals("25", literal.value) // sin comillas
        assertEquals("string", varDecl.varType)
    }

    @Test
    fun variableDeclarationBuilder_should_handle_empty_string() {
        // Arrange - tokens que representan let vacio = "";
        val builder = VariableDeclarationBuilder()
        val tokens =
            listOf(
                Token(ModifierType, "let", 1, 1),
                Token(IdentifierType, "vacio", 1, 5),
                Token(AssignmentType, "=", 1, 11),
                Token(LiteralString, "\"\"", 1, 13),
                Token(PunctuationType, ";", 1, 15),
            )

        // Act
        val node = builder.buildNode(tokens)

        // Assert
        assertTrue(node is VariableDeclarationNode)

        val varDecl = node
        val literal = varDecl.value as LiteralNode

        assertEquals("", literal.value) // string vacío sin comillas
    }

    @Test
    fun `test explicit type declaration without initialization`() {
        val tokens =
            listOf(
                Token(ModifierType, "let", 1, 1),
                Token(IdentifierType, "nombre", 1, 5),
                Token(PunctuationType, ":", 1, 11),
                Token(StringType, "string", 1, 13),
                Token(PunctuationType, ";", 1, 19),
            )

        val node = builder.buildNode(tokens)
        assertTrue(node is VariableDeclarationNode)
        val varDecl = node as VariableDeclarationNode

        assertEquals("nombre", varDecl.identifier.name)
        assertEquals("string", varDecl.varType)
        assertNull(varDecl.value)
    }

    @Test
    fun `test explicit type declaration with initialization`() {
        val tokens =
            listOf(
                Token(ModifierType, "let", 1, 1),
                Token(IdentifierType, "edad", 1, 5),
                Token(PunctuationType, ":", 1, 9),
                Token(NumberType, "number", 1, 11),
                Token(AssignmentType, "=", 1, 18),
                Token(LiteralNumber, "25", 1, 20),
                Token(PunctuationType, ";", 1, 22),
            )

        val node = builder.buildNode(tokens)
        assertTrue(node is VariableDeclarationNode)
        val varDecl = node as VariableDeclarationNode

        assertEquals("edad", varDecl.identifier.name)
        assertEquals("number", varDecl.varType)
        assertNotNull(varDecl.value)
        assertTrue(varDecl.value is LiteralNode)
        assertEquals("25", (varDecl.value as LiteralNode).value)
    }

    @Test
    fun `test binary expression with two operands`() {
        val tokens =
            listOf(
                Token(ModifierType, "let", 1, 1),
                Token(IdentifierType, "suma", 1, 5),
                Token(AssignmentType, "=", 1, 10),
                Token(LiteralNumber, "5", 1, 12),
                Token(PunctuationType, "+", 1, 14),
                Token(LiteralNumber, "3", 1, 16),
                Token(PunctuationType, ";", 1, 17),
            )

        val node = builder.buildNode(tokens)
        assertTrue(node is VariableDeclarationNode)
        val varDecl = node as VariableDeclarationNode

        assertTrue(varDecl.value is BinaryOpNode)
        val binaryOp = varDecl.value as BinaryOpNode
        assertEquals("+", binaryOp.operator)
        assertTrue(binaryOp.left is LiteralNode)
        assertTrue(binaryOp.right is LiteralNode)
    }

    @Test
    fun `test binary expression with multiple operands`() {
        val tokens =
            listOf(
                Token(ModifierType, "let", 1, 1),
                Token(IdentifierType, "calculo", 1, 5),
                Token(AssignmentType, "=", 1, 13),
                Token(LiteralNumber, "10", 1, 15),
                Token(PunctuationType, "+", 1, 18),
                Token(LiteralNumber, "5", 1, 20),
                Token(PunctuationType, "*", 1, 22),
                Token(LiteralNumber, "2", 1, 24),
                Token(PunctuationType, ";", 1, 25),
            )

        val node = builder.buildNode(tokens)
        assertTrue(node is VariableDeclarationNode)
        val varDecl = node as VariableDeclarationNode

        assertTrue(varDecl.value is BinaryOpNode)
        val binaryOp = varDecl.value as BinaryOpNode
        assertEquals("*", binaryOp.operator)
        assertTrue(binaryOp.right is LiteralNode)
        assertTrue(binaryOp.left is BinaryOpNode)
    }

    @Test
    fun `test variable declaration with identifier as value`() {
        val tokens =
            listOf(
                Token(ModifierType, "let", 1, 1),
                Token(IdentifierType, "y", 1, 5),
                Token(AssignmentType, "=", 1, 7),
                Token(IdentifierType, "x", 1, 9),
                Token(PunctuationType, ";", 1, 10),
            )

        val node = builder.buildNode(tokens)
        assertTrue(node is VariableDeclarationNode)
        val varDecl = node as VariableDeclarationNode

        assertEquals("unknown", varDecl.varType)
        assertTrue(varDecl.value is IdentifierNode)
        assertEquals("x", (varDecl.value as IdentifierNode).name)
    }

    @Test
    fun `test error on invalid binary expression`() {
        val tokens =
            listOf(
                Token(ModifierType, "let", 1, 1),
                Token(IdentifierType, "error", 1, 5),
                Token(AssignmentType, "=", 1, 11),
                Token(PunctuationType, "+", 1, 13),
                Token(PunctuationType, ";", 1, 14),
            )

        assertFailsWith<IllegalStateException>(
            message = "Expresión binaria debe tener al menos 3 tokens",
        ) {
            builder.buildNode(tokens)
        }
    }

    @Test
    fun `test error on invalid token in expression`() {
        val tokens =
            listOf(
                Token(ModifierType, "let", 1, 1),
                Token(IdentifierType, "error", 1, 5),
                Token(AssignmentType, "=", 1, 11),
                Token(PunctuationType, "{", 1, 13),
                Token(PunctuationType, ";", 1, 14),
            )

        assertFailsWith<IllegalStateException>(
            message = "Token no válido en expresión: punctuation",
        ) {
            builder.buildNode(tokens)
        }
    }

    @Test
    fun `test error on missing value tokens`() {
        val tokens =
            listOf(
                Token(ModifierType, "let", 1, 1),
                Token(IdentifierType, "error", 1, 5),
                Token(AssignmentType, "=", 1, 11),
                Token(PunctuationType, ";", 1, 12),
            )

        assertFailsWith<RuntimeException>(
            message = "No hay valor para la variable: $tokens",
        ) {
            builder.buildNode(tokens)
        }
    }
}
