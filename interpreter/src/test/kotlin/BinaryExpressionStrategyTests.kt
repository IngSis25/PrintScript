package test.interpreterTest

import org.example.ast.BinaryOpNode
import org.example.ast.LiteralNode
import org.example.output.Output
import org.example.strategy.binaryExpressionStrategy
import org.example.strategy.literalStrategy
import org.example.util.Services
import kotlin.test.*

class BinaryExpressionStrategyTests {
    private fun createMockServices(): Services {
        val mockOutput =
            object : Output {
                override infix fun write(msg: String) {}
            }
        return Services(
            context = emptyMap(),
            output = mockOutput,
            visit = { services, node ->
                // Mock visit que simula evaluación de literales
                when (node) {
                    is LiteralNode -> literalStrategy.visit(services, node)
                    else -> null
                }
            },
        )
    }

    @Test
    fun binaryExpressionStrategy_should_handle_addition() {
        // Arrange
        val leftNode =
            LiteralNode(
                "12",
                object : org.example.TokenType {
                    override val name = "NUMBER"
                },
            )
        val rightNode =
            LiteralNode(
                "8",
                object : org.example.TokenType {
                    override val name = "NUMBER"
                },
            )
        val binaryNode = BinaryOpNode(leftNode, "+", rightNode)
        val services = createMockServices()

        // Act
        val result = binaryExpressionStrategy.visit(services, binaryNode)

        // Assert
        assertEquals(20.0, result)
    }

    @Test
    fun binaryExpressionStrategy_should_handle_subtraction() {
        // Arrange
        val leftNode =
            LiteralNode(
                "15",
                object : org.example.TokenType {
                    override val name = "NUMBER"
                },
            )
        val rightNode =
            LiteralNode(
                "5",
                object : org.example.TokenType {
                    override val name = "NUMBER"
                },
            )
        val binaryNode = BinaryOpNode(leftNode, "-", rightNode)
        val services = createMockServices()

        // Act
        val result = binaryExpressionStrategy.visit(services, binaryNode)

        // Assert
        assertEquals(10.0, result)
    }

    @Test
    fun binaryExpressionStrategy_should_handle_multiplication() {
        // Arrange
        val leftNode =
            LiteralNode(
                "6",
                object : org.example.TokenType {
                    override val name = "NUMBER"
                },
            )
        val rightNode =
            LiteralNode(
                "7",
                object : org.example.TokenType {
                    override val name = "NUMBER"
                },
            )
        val binaryNode = BinaryOpNode(leftNode, "*", rightNode)
        val services = createMockServices()

        // Act
        val result = binaryExpressionStrategy.visit(services, binaryNode)

        // Assert
        assertEquals(42.0, result)
    }

    @Test
    fun binaryExpressionStrategy_should_handle_division() {
        // Arrange
        val leftNode =
            LiteralNode(
                "20",
                object : org.example.TokenType {
                    override val name = "NUMBER"
                },
            )
        val rightNode =
            LiteralNode(
                "4",
                object : org.example.TokenType {
                    override val name = "NUMBER"
                },
            )
        val binaryNode = BinaryOpNode(leftNode, "/", rightNode)
        val services = createMockServices()

        // Act
        val result = binaryExpressionStrategy.visit(services, binaryNode)

        // Assert
        assertEquals(5.0, result)
    }

    @Test
    fun binaryExpressionStrategy_should_handle_decimal_operations() {
        // Arrange
        val leftNode =
            LiteralNode(
                "3.5",
                object : org.example.TokenType {
                    override val name = "NUMBER"
                },
            )
        val rightNode =
            LiteralNode(
                "2.5",
                object : org.example.TokenType {
                    override val name = "NUMBER"
                },
            )
        val binaryNode = BinaryOpNode(leftNode, "+", rightNode)
        val services = createMockServices()

        // Act
        val result = binaryExpressionStrategy.visit(services, binaryNode)

        // Assert
        assertEquals(6.0, result)
    }

    @Test
    fun binaryExpressionStrategy_should_throw_exception_for_division_by_zero() {
        // Arrange
        val leftNode =
            LiteralNode(
                "10",
                object : org.example.TokenType {
                    override val name = "NUMBER"
                },
            )
        val rightNode =
            LiteralNode(
                "0",
                object : org.example.TokenType {
                    override val name = "NUMBER"
                },
            )
        val binaryNode = BinaryOpNode(leftNode, "/", rightNode)
        val services = createMockServices()

        // Act & Assert
        assertFailsWith<ArithmeticException> {
            binaryExpressionStrategy.visit(services, binaryNode)
        }
    }

    @Test
    fun binaryExpressionStrategy_should_throw_exception_for_unsupported_operator() {
        // Arrange
        val leftNode =
            LiteralNode(
                "5",
                object : org.example.TokenType {
                    override val name = "NUMBER"
                },
            )
        val rightNode =
            LiteralNode(
                "3",
                object : org.example.TokenType {
                    override val name = "NUMBER"
                },
            )
        val binaryNode = BinaryOpNode(leftNode, "%", rightNode) // operador no soportado
        val services = createMockServices()

        // Act & Assert
        assertFailsWith<RuntimeException> {
            binaryExpressionStrategy.visit(services, binaryNode)
        }
    }

    @Test
    fun binaryExpressionStrategy_should_throw_exception_for_non_numeric_operands() {
        // Arrange - simular que visit devuelve string en lugar de número
        val mockOutput =
            object : Output {
                override infix fun write(msg: String) {}
            }
        val servicesWithStringResult =
            Services(
                context = emptyMap(),
                output = mockOutput,
                visit = { _, _ -> "not a number" }, // devuelve string
            )

        val leftNode =
            LiteralNode(
                "5",
                object : org.example.TokenType {
                    override val name = "NUMBER"
                },
            )
        val rightNode =
            LiteralNode(
                "3",
                object : org.example.TokenType {
                    override val name = "NUMBER"
                },
            )
        val binaryNode = BinaryOpNode(leftNode, "+", rightNode)

        // Act & Assert
        assertFailsWith<RuntimeException> {
            binaryExpressionStrategy.visit(servicesWithStringResult, binaryNode)
        }
    }
}
