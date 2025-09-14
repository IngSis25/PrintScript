package test.parserTest

import main.kotlin.parser.ParseResult
import org.example.LiteralNumber
import org.example.ast.LiteralNode
import kotlin.test.*

class ParseResultTests {
    @Test
    fun parseResultSuccess_should_contain_node_and_position() {
        // Arrange
        val node = LiteralNode("42", LiteralNumber)
        val position = 5

        // Act
        val result = ParseResult.Success(node, position)

        // Assert
        assertEquals(node, result.node)
        assertEquals(position, result.nextPosition)
    }

    @Test
    fun parseResultFailure_should_contain_message_and_position() {
        // Arrange
        val message = "Syntax error at position 3"
        val position = 3

        // Act
        val result = ParseResult.Failure(message, position)

        // Assert
        assertEquals(message, result.message)
        assertEquals(position, result.position)
    }

    @Test
    fun parseResultSuccess_should_be_accessible_via_properties() {
        // Arrange
        val node = LiteralNode("test", LiteralNumber)
        val position = 10

        // Act
        val result = ParseResult.Success(node, position)

        // Assert
        assertNotNull(result.node)
        assertTrue(true)
        assertEquals(10, result.nextPosition)
    }

    @Test
    fun parseResultFailure_should_be_accessible_via_properties() {
        // Arrange
        val message = "Unexpected token"
        val position = 7

        // Act
        val result = ParseResult.Failure(message, position)

        // Assert
        assertNotNull(result.message)
        assertTrue(result.message.contains("Unexpected"))
        assertEquals(7, result.position)
    }
}
