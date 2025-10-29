package test.parserTest

import builders.ConstBuilder
import main.kotlin.lexer.*
import org.example.ast.*
import types.*
import kotlin.test.*

class ConstBuilderTests {
    private var constBuilder = ConstBuilder()

    @Test
    fun should_build_const_declaration_with_number() {
        val tokens =
            listOf(
                Token(ModifierType, "const", 1, 1),
                Token(IdentifierType, "count", 1, 7),
                Token(AssignmentType, "=", 1, 13),
                Token(LiteralNumber, "42", 1, 15),
                Token(PunctuationType, ";", 1, 17),
            )

        val result = constBuilder.buildNode(tokens)

        assertTrue(result is VariableDeclarationNode)
        val varDecl = result as VariableDeclarationNode

        assertTrue(varDecl.identifier is IdentifierNode)
        assertEquals("count", (varDecl.identifier as IdentifierNode).name)
        assertEquals("const", varDecl.varType)

        assertTrue(varDecl.value is LiteralNode)
        val literal = varDecl.value as LiteralNode
        assertEquals("42", literal.value)
        assertEquals(LiteralNumber, literal.literalType)
    }

    @Test
    fun should_build_const_declaration_with_different_identifier() {
        val tokens =
            listOf(
                Token(ModifierType, "const", 1, 1),
                Token(IdentifierType, "MAX_SIZE", 1, 7),
                Token(AssignmentType, "=", 1, 16),
                Token(LiteralNumber, "100", 1, 18),
                Token(PunctuationType, ";", 1, 21),
            )

        val result = constBuilder.buildNode(tokens)

        assertTrue(result is VariableDeclarationNode)
        val varDecl = result as VariableDeclarationNode

        assertTrue(varDecl.identifier is IdentifierNode)
        assertEquals("MAX_SIZE", (varDecl.identifier as IdentifierNode).name)
        assertEquals("const", varDecl.varType)

        assertTrue(varDecl.value is LiteralNode)
        val literal = varDecl.value as LiteralNode
        assertEquals("100", literal.value)
        assertEquals(LiteralNumber, literal.literalType)
    }
}
