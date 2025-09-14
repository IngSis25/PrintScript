package test.interpreterTest

import org.example.ast.LiteralNode
import org.example.ast.PrintlnNode
import org.example.output.Output
import org.example.strategy.literalStrategy
import org.example.strategy.printlnStrategy
import org.example.util.Services
import kotlin.test.*

class PrintlnStrategyTests {
    @Test
    fun printlnStrategy_should_print_string_literal() {
        // Arrange
        val literalNode =
            LiteralNode(
                "\"hello world\"",
                object : org.example.TokenType {
                    override val name = "STRING"
                },
            )
        val printNode = PrintlnNode(literalNode)

        var capturedOutput = ""
        val mockOutput =
            object : Output {
                override infix fun write(msg: String) {
                    capturedOutput = msg
                }
            }

        val services =
            Services(
                context = emptyMap(),
                output = mockOutput,
                visit = { services, node ->
                    when (node) {
                        is LiteralNode -> literalStrategy.visit(services, node)
                        else -> null
                    }
                },
            )

        // Act
        val result = printlnStrategy.visit(services, printNode)

        // Assert
        assertEquals("hello world", capturedOutput) // sin comillas
        assertTrue(result is Services) // println ahora devuelve Services para preservar contexto
    }

    @Test
    fun printlnStrategy_should_print_number_literal() {
        // Arrange
        val literalNode =
            LiteralNode(
                "42",
                object : org.example.TokenType {
                    override val name = "NUMBER"
                },
            )
        val printNode = PrintlnNode(literalNode)

        var capturedOutput = ""
        val mockOutput =
            object : Output {
                override infix fun write(msg: String) {
                    capturedOutput = msg
                }
            }

        val services =
            Services(
                context = emptyMap(),
                output = mockOutput,
                visit = { services, node ->
                    when (node) {
                        is LiteralNode -> literalStrategy.visit(services, node)
                        else -> null
                    }
                },
            )

        // Act
        val result = printlnStrategy.visit(services, printNode)

        // Assert
        assertEquals("42.0", capturedOutput) // número como string
        assertTrue(result is Services) // println ahora devuelve Services para preservar contexto
    }

    @Test
    fun printlnStrategy_should_print_decimal_number() {
        // Arrange
        val literalNode =
            LiteralNode(
                "3.14",
                object : org.example.TokenType {
                    override val name = "NUMBER"
                },
            )
        val printNode = PrintlnNode(literalNode)

        var capturedOutput = ""
        val mockOutput =
            object : Output {
                override infix fun write(msg: String) {
                    capturedOutput = msg
                }
            }

        val services =
            Services(
                context = emptyMap(),
                output = mockOutput,
                visit = { services, node ->
                    when (node) {
                        is LiteralNode -> literalStrategy.visit(services, node)
                        else -> null
                    }
                },
            )

        // Act
        val result = printlnStrategy.visit(services, printNode)

        // Assert
        assertEquals("3.14", capturedOutput)
        assertTrue(result is Services) // println ahora devuelve Services para preservar contexto
    }

    @Test
    fun printlnStrategy_should_print_empty_string() {
        // Arrange
        val literalNode =
            LiteralNode(
                "\"\"",
                object : org.example.TokenType {
                    override val name = "STRING"
                },
            )
        val printNode = PrintlnNode(literalNode)

        var capturedOutput = ""
        val mockOutput =
            object : Output {
                override infix fun write(msg: String) {
                    capturedOutput = msg
                }
            }

        val services =
            Services(
                context = emptyMap(),
                output = mockOutput,
                visit = { services, node ->
                    when (node) {
                        is LiteralNode -> literalStrategy.visit(services, node)
                        else -> null
                    }
                },
            )

        // Act
        val result = printlnStrategy.visit(services, printNode)

        // Assert
        assertEquals("", capturedOutput) // string vacío
        assertTrue(result is Services) // println ahora devuelve Services para preservar contexto
    }

    @Test
    fun printlnStrategy_should_convert_any_value_to_string() {
        // Arrange - simular que visit devuelve un objeto cualquiera
        val literalNode =
            LiteralNode(
                "test",
                object : org.example.TokenType {
                    override val name = "STRING"
                },
            )
        val printNode = PrintlnNode(literalNode)

        var capturedOutput = ""
        val mockOutput =
            object : Output {
                override infix fun write(msg: String) {
                    capturedOutput = msg
                }
            }

        val services =
            Services(
                context = emptyMap(),
                output = mockOutput,
                visit = { _, _ -> 123 }, // devuelve un entero
            )

        // Act
        val result = printlnStrategy.visit(services, printNode)

        // Assert
        assertEquals("123", capturedOutput) // convertido a string
        assertTrue(result is Services) // println ahora devuelve Services para preservar contexto
    }
}
