package test.parserTest

import builders.VariableDeclarationBuilder
import main.kotlin.lexer.*
import org.example.ast.*
import types.AssignmentType
import types.ModifierType
import types.PunctuationType
import kotlin.test.*

class VariableDeclarationBuilderTests {
    @Test
    fun variableDeclarationBuilder_should_create_variable_declaration_node() {
        // Arrange - tokens que representan let nombre = "Juan";
        val builder = VariableDeclarationBuilder()
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

        val varDecl = node as VariableDeclarationNode

        // Verificar identifier
        assertTrue(varDecl.identifier is IdentifierNode)
        val identifier = varDecl.identifier as IdentifierNode
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

        val varDecl = node as VariableDeclarationNode
        val identifier = varDecl.identifier as IdentifierNode
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

        val varDecl = node as VariableDeclarationNode
        val literal = varDecl.value as LiteralNode

        assertEquals("", literal.value) // string vacío sin comillas
    }
}
