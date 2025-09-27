package test.interpreterTest

import org.example.ast.*
import org.example.output.Output
import org.example.strategy.PreConfiguredProviders
import org.example.util.Services
import kotlin.test.*

class PreConfiguredProvidersTests {
    private fun createMockServices(context: Map<String, Any?> = emptyMap()): Services {
        val mockOutput =
            object : Output {
                override infix fun write(msg: String) {}
            }
        return Services(
            context = context,
            output = mockOutput,
            visit = { services, node ->
                // Mock visit que usa el mismo provider para recursión
                val strategy = PreConfiguredProviders.VERSION_1_0 getStrategyFor node
                strategy?.visit(services, node)
            },
        )
    }

    @Test
    fun preConfiguredProvider_should_have_literal_strategy() {
        // Arrange
        val provider = PreConfiguredProviders.VERSION_1_0
        val literalNode =
            LiteralNode(
                "\"test\"",
                object : org.example.TokenType {
                    override val name = "STRING"
                },
            )
        val services = createMockServices()

        // Act
        val strategy = provider getStrategyFor literalNode
        val result = strategy?.visit(services, literalNode)

        // Assert
        assertNotNull(strategy)
        assertEquals("test", result)
    }

    @Test
    fun preConfiguredProvider_should_have_identifier_strategy() {
        // Arrange
        val provider = PreConfiguredProviders.VERSION_1_0
        val context = mapOf("variable" to "valor")
        val identifierNode = IdentifierNode("variable")
        val services = createMockServices(context)

        // Act
        val strategy = provider getStrategyFor identifierNode
        val result = strategy?.visit(services, identifierNode)

        // Assert
        assertNotNull(strategy)
        assertEquals("valor", result)
    }

    @Test
    fun preConfiguredProvider_should_have_variable_declaration_strategy() {
        // Arrange
        val provider = PreConfiguredProviders.VERSION_1_0
        val identifier = IdentifierNode("nueva")
        val value =
            LiteralNode(
                "\"valor\"",
                object : org.example.TokenType {
                    override val name = "STRING"
                },
            )
        val varDecl = VariableDeclarationNode(identifier, "string", value)
        val services = createMockServices()

        // Act
        val strategy = provider getStrategyFor varDecl
        val result = strategy?.visit(services, varDecl)

        // Assert
        assertNotNull(strategy)
        assertTrue(result is Services)
        val newServices = result as Services
        assertTrue(newServices.context.containsKey("nueva"))
    }

    @Test
    fun preConfiguredProvider_should_have_assignment_strategy() {
        // Arrange
        val provider = PreConfiguredProviders.VERSION_1_0
        val context = mapOf("existente" to "valorAnterior")
        val identifier = IdentifierNode("existente")
        val newValue =
            LiteralNode(
                "\"nuevoValor\"",
                object : org.example.TokenType {
                    override val name = "STRING"
                },
            )
        val assignment = AssignmentNode(identifier, newValue)
        val services = createMockServices(context)

        // Act
        val strategy = provider getStrategyFor assignment
        val result = strategy?.visit(services, assignment)

        // Assert
        assertNotNull(strategy)
        assertTrue(result is Services)
        val newServices = result as Services
        assertEquals("nuevoValor", newServices.context["existente"])
    }

    @Test
    fun preConfiguredProvider_should_have_binary_expression_strategy() {
        // Arrange
        val provider = PreConfiguredProviders.VERSION_1_0
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
        val strategy = provider getStrategyFor binaryNode
        val result = strategy?.visit(services, binaryNode)

        // Assert
        assertNotNull(strategy)
        assertEquals(20.0, result)
    }

    @Test
    fun preConfiguredProvider_should_have_println_strategy() {
        // Arrange
        val provider = PreConfiguredProviders.VERSION_1_0
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
                    val strategy = provider getStrategyFor node
                    strategy?.visit(services, node)
                },
            )

        val literalNode =
            LiteralNode(
                "\"hello\"",
                object : org.example.TokenType {
                    override val name = "STRING"
                },
            )
        val printNode = PrintlnNode(literalNode)

        // Act
        val strategy = provider getStrategyFor printNode
        val result = strategy?.visit(services, printNode)

        // Assert
        assertNotNull(strategy)
        assertTrue(result is Services) // println ahora devuelve Services para preservar contexto
        assertEquals("hello\n", capturedOutput) // con newline
    }

    @Test
    fun preConfiguredProvider_should_have_all_required_strategies() {
        // Arrange
        val provider = PreConfiguredProviders.VERSION_1_0

        // Crear nodos de todos los tipos
        val literalNode =
            LiteralNode(
                "test",
                object : org.example.TokenType {
                    override val name = "STRING"
                },
            )
        val identifierNode = IdentifierNode("var")
        val varDeclNode = VariableDeclarationNode(identifierNode, "string", literalNode)
        val assignmentNode = AssignmentNode(identifierNode, literalNode)
        val binaryNode = BinaryOpNode(literalNode, "+", literalNode)
        val printNode = PrintlnNode(literalNode)

        // Act & Assert - todas las strategies deberían existir
        assertNotNull(provider getStrategyFor literalNode, "LiteralStrategy missing")
        assertNotNull(provider getStrategyFor identifierNode, "IdentifierStrategy missing")
        assertNotNull(provider getStrategyFor varDeclNode, "VariableDeclarationStrategy missing")
        assertNotNull(provider getStrategyFor assignmentNode, "AssignmentStrategy missing")
        assertNotNull(provider getStrategyFor binaryNode, "BinaryExpressionStrategy missing")
        assertNotNull(provider getStrategyFor printNode, "PrintlnStrategy missing")
    }

    @Test
    fun preConfiguredProvider_should_return_null_for_unknown_node_type() {
        // Arrange
        val provider = PreConfiguredProviders.VERSION_1_0
        val unknownNode = object : ASTNode {} // nodo no soportado

        // Act
        val strategy = provider getStrategyFor unknownNode

        // Assert
        assertNull(strategy) // no debería encontrar strategy para tipo desconocido
    }
}
