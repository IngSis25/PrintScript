package test.interpreterTest

import org.example.ast.LiteralNode
import org.example.output.Output
import org.example.strategy.literalStrategy
import org.example.util.Services
import kotlin.test.*

class LiteralStrategyTests {
    @Test
    fun literalStrategy_should_handle_string_with_double_quotes() {
        // Arrange
        val node =
            LiteralNode(
                "\"hello\"",
                object : TokenType {
                    override val name = "STRING"
                },
            )
        val mockOutput =
            object : Output {
                override infix fun write(msg: String) {}
            }
        val services =
            Services(
                context = emptyMap(),
                output = mockOutput,
                visit = { _, _ -> null },
            )

        // Act
        val result = literalStrategy.visit(services, node)

        // Assert
        assertEquals("hello", result) // sin comillas
    }

    @Test
    fun literalStrategy_should_handle_string_with_single_quotes() {
        // Arrange
        val node =
            LiteralNode(
                "'world'",
                object : TokenType {
                    override val name = "STRING"
                },
            )
        val mockOutput =
            object : Output {
                override infix fun write(msg: String) {}
            }
        val services =
            Services(
                context = emptyMap(),
                output = mockOutput,
                visit = { _, _ -> null },
            )

        // Act
        val result = literalStrategy.visit(services, node)

        // Assert
        assertEquals("world", result) // sin comillas
    }

    @Test
    fun literalStrategy_should_handle_integer_number() {
        // Arrange
        val node =
            LiteralNode(
                "42",
                object : TokenType {
                    override val name = "NUMBER"
                },
            )
        val mockOutput =
            object : Output {
                override infix fun write(msg: String) {}
            }
        val services =
            Services(
                context = emptyMap(),
                output = mockOutput,
                visit = { _, _ -> null },
            )

        // Act
        val result = literalStrategy.visit(services, node)

        // Assert
        assertEquals(42.0, result)
    }

    @Test
    fun literalStrategy_should_handle_decimal_number() {
        // Arrange
        val node =
            LiteralNode(
                "3.14",
                object : TokenType {
                    override val name = "NUMBER"
                },
            )
        val mockOutput =
            object : Output {
                override infix fun write(msg: String) {}
            }
        val services =
            Services(
                context = emptyMap(),
                output = mockOutput,
                visit = { _, _ -> null },
            )

        // Act
        val result = literalStrategy.visit(services, node)

        // Assert
        assertEquals(3.14, result)
    }

    @Test
    fun literalStrategy_should_handle_raw_string_without_quotes() {
        // Arrange
        val node =
            LiteralNode(
                "rawtext",
                object : TokenType {
                    override val name = "STRING"
                },
            )
        val mockOutput =
            object : Output {
                override infix fun write(msg: String) {}
            }
        val services =
            Services(
                context = emptyMap(),
                output = mockOutput,
                visit = { _, _ -> null },
            )

        // Act
        val result = literalStrategy.visit(services, node)

        // Assert
        assertEquals("rawtext", result) // se devuelve tal como está
    }

    @Test
    fun literalStrategy_should_handle_empty_string() {
        // Arrange
        val node =
            LiteralNode(
                "\"\"",
                object : TokenType {
                    override val name = "STRING"
                },
            )
        val mockOutput =
            object : Output {
                override infix fun write(msg: String) {}
            }
        val services =
            Services(
                context = emptyMap(),
                output = mockOutput,
                visit = { _, _ -> null },
            )

        // Act
        val result = literalStrategy.visit(services, node)

        // Assert
        assertEquals("", result) // string vacío
    }
}
