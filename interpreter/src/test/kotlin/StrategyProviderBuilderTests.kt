package test.interpreterTest

import org.example.ast.LiteralNode
import org.example.ast.PrintlnNode
import org.example.builder.StrategyProviderBuilder
import org.example.output.Output
import org.example.strategy.Strategy
import org.example.strategy.literalStrategy
import org.example.strategy.printlnStrategy
import org.example.util.Services
import kotlin.test.*

class StrategyProviderBuilderTests {
    private fun createMockServices(): Services {
        val mockOutput =
            object : Output {
                override infix fun write(msg: String) {}
            }
        return Services(
            context = emptyMap(),
            output = mockOutput,
            visit = { _, _ -> null },
        )
    }

    @Test
    fun strategyProviderBuilder_should_add_strategy_with_infix() {
        // Arrange
        val builder = StrategyProviderBuilder()

        // Act - usando infix function
        builder addStrategy literalStrategy
        val provider = builder.build()

        // Assert
        val literalNode =
            LiteralNode(
                "\"test\"",
                object : org.example.TokenType {
                    override val name = "STRING"
                },
            )
        val strategy = provider getStrategyFor literalNode

        assertNotNull(strategy)
        assertEquals("test", strategy.visit(createMockServices(), literalNode))
    }

    @Test
    fun strategyProviderBuilder_should_add_strategy_with_explicit_class() {
        // Arrange
        val builder = StrategyProviderBuilder()

        // Act - usando versión explícita
        builder.addStrategy(LiteralNode::class.java, literalStrategy)
        val provider = builder.build()

        // Assert
        val literalNode =
            LiteralNode(
                "42",
                object : org.example.TokenType {
                    override val name = "NUMBER"
                },
            )
        val strategy = provider getStrategyFor literalNode

        assertNotNull(strategy)
        assertEquals(42.0, strategy.visit(createMockServices(), literalNode))
    }

    @Test
    fun strategyProviderBuilder_should_add_multiple_strategies() {
        // Arrange
        val builder = StrategyProviderBuilder()

        // Act - agregar múltiples strategies
        builder addStrategy literalStrategy
        builder addStrategy printlnStrategy
        val provider = builder.build()

        // Assert - ambas strategies deberían estar disponibles
        val literalNode =
            LiteralNode(
                "\"hello\"",
                object : org.example.TokenType {
                    override val name = "STRING"
                },
            )
        val printNode = PrintlnNode(literalNode)

        val literalStrategy = provider getStrategyFor literalNode
        val printStrategy = provider getStrategyFor printNode

        assertNotNull(literalStrategy)
        assertNotNull(printStrategy)
    }

    @Test
    fun strategyProviderBuilder_should_override_strategy_when_added_twice() {
        // Arrange
        val builder = StrategyProviderBuilder()
        val customLiteralStrategy = Strategy<LiteralNode> { _, _ -> "custom result" }

        // Act - agregar strategy original, luego custom
        builder addStrategy literalStrategy
        builder addStrategy customLiteralStrategy // debería reemplazar la anterior
        val provider = builder.build()

        // Assert
        val literalNode =
            LiteralNode(
                "\"test\"",
                object : org.example.TokenType {
                    override val name = "STRING"
                },
            )
        val strategy = provider getStrategyFor literalNode

        assertNotNull(strategy)
        assertEquals("custom result", strategy.visit(createMockServices(), literalNode))
    }

    @Test
    fun strategyProviderBuilder_should_build_empty_provider() {
        // Arrange
        val builder = StrategyProviderBuilder()

        // Act - build sin agregar strategies
        val provider = builder.build()

        // Assert
        val literalNode =
            LiteralNode(
                "\"test\"",
                object : org.example.TokenType {
                    override val name = "STRING"
                },
            )
        val strategy = provider getStrategyFor literalNode

        assertNull(strategy) // no debería encontrar strategy
    }

    @Test
    fun strategyProviderBuilder_should_handle_different_node_types() {
        // Arrange
        val builder = StrategyProviderBuilder()
        val customPrintStrategy = Strategy<PrintlnNode> { _, _ -> "printed" }

        // Act
        builder addStrategy literalStrategy
        builder addStrategy customPrintStrategy
        val provider = builder.build()

        // Assert - cada tipo debería tener su strategy correcta
        val literalNode =
            LiteralNode(
                "\"test\"",
                object : org.example.TokenType {
                    override val name = "STRING"
                },
            )
        val printNode = PrintlnNode(literalNode)

        val literalResult = (provider getStrategyFor literalNode)?.visit(createMockServices(), literalNode)
        val printResult = (provider getStrategyFor printNode)?.visit(createMockServices(), printNode)

        assertEquals("test", literalResult)
        assertEquals("printed", printResult)
    }

    @Test
    fun strategyProviderBuilder_should_support_fluent_interface() {
        // Arrange & Act - usando el builder de forma fluida
        val provider =
            StrategyProviderBuilder()
                .apply { this addStrategy literalStrategy }
                .apply { this addStrategy printlnStrategy }
                .build()

        // Assert
        val literalNode =
            LiteralNode(
                "\"fluent\"",
                object : org.example.TokenType {
                    override val name = "STRING"
                },
            )
        val strategy = provider getStrategyFor literalNode

        assertNotNull(strategy)
        assertEquals("fluent", strategy.visit(createMockServices(), literalNode))
    }
}
