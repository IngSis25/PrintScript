package test.parserTest

import DefaultParser
import builders.PrintBuilder
import main.kotlin.lexer.*
import org.example.ast.LiteralNode
import org.example.ast.PrintlnNode
import rules.PrintlnRule
import rules.RuleMatcher
import types.PunctuationType
import kotlin.test.*

class DefaultParserTest {
    @Test
    fun parse_simple_println_with_string() {
        // Arrange - similar a como haces en MainTest
        val tokens =
            listOf(
                Token(IdentifierType, "println", 1, 1),
                Token(PunctuationType, "(", 1, 8),
                Token(LiteralString, "\"hola\"", 1, 9),
                Token(PunctuationType, ")", 1, 15),
                Token(PunctuationType, ";", 1, 16),
            )

        val ruleMatcher = RuleMatcher(listOf(PrintlnRule(PrintBuilder())))
        val parser = DefaultParser(ruleMatcher)

        // Act
        val ast = parser.parse(tokens)

        // Assert - siguiendo tu estilo de verificaciones
        assertEquals(1, ast.size)
        assertTrue(ast[0] is PrintlnNode)

        val printNode = ast[0] as PrintlnNode
        assertTrue(printNode.value is LiteralNode)

        val literal = printNode.value as LiteralNode
        assertEquals("hola", literal.value) // sin comillas
        assertEquals(LiteralString, literal.literalType)
    }

    @Test
    fun parse_simple_println_with_number() {
        // Arrange
        val tokens =
            listOf(
                Token(IdentifierType, "println", 1, 1),
                Token(PunctuationType, "(", 1, 8),
                Token(LiteralNumber, "42", 1, 9),
                Token(PunctuationType, ")", 1, 11),
                Token(PunctuationType, ";", 1, 12),
            )

        val ruleMatcher = RuleMatcher(listOf(PrintlnRule(PrintBuilder())))
        val parser = DefaultParser(ruleMatcher)

        // Act
        val ast = parser.parse(tokens)

        // Assert
        assertEquals(1, ast.size)
        assertTrue(ast[0] is PrintlnNode)

        val printNode = ast[0] as PrintlnNode
        assertTrue(printNode.value is LiteralNode)

        val literal = printNode.value as LiteralNode
        assertEquals("42", literal.value)
        assertEquals(LiteralNumber, literal.literalType)
    }
}
