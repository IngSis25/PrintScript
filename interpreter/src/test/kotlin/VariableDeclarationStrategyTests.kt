package test.interpreterTest

import org.example.ast.IdentifierNode
import org.example.ast.LiteralNode
import org.example.ast.VariableDeclarationNode
import org.example.output.Output
import org.example.strategy.literalStrategy
import org.example.strategy.variableDeclarationStrategy
import org.example.util.Services
import kotlin.test.*

class VariableDeclarationStrategyTests {
    private fun createMockServices(initialContext: Map<String, Any?> = emptyMap()): Services {
        val mockOutput =
            object : Output {
                override infix fun write(msg: String) {}
            }
        return Services(
            context = initialContext,
            output = mockOutput,
            visit = { services, node ->
                when (node) {
                    is LiteralNode -> literalStrategy.visit(services, node)
                    else -> null
                }
            },
        )
    }

    @Test
    fun variableDeclarationStrategy_should_declare_variable_with_string_value() {
        // Arrange
        val identifier = IdentifierNode("nombre")
        val value =
            LiteralNode(
                "\"Juan\"",
                object : org.example.TokenType {
                    override val name = "STRING"
                },
            )
        val varDecl = VariableDeclarationNode(identifier, "string", value)
        val services = createMockServices()

        // Act
        val result = variableDeclarationStrategy.visit(services, varDecl)

        // Assert
        assertTrue(result is Services)
        val newServices = result as Services
        assertTrue(newServices.context.containsKey("nombre"))
        assertEquals("Juan", newServices.context["nombre"]) // sin comillas
    }

    @Test
    fun variableDeclarationStrategy_should_declare_variable_with_number_value() {
        // Arrange
        val identifier = IdentifierNode("edad")
        val value =
            LiteralNode(
                "25",
                object : org.example.TokenType {
                    override val name = "NUMBER"
                },
            )
        val varDecl = VariableDeclarationNode(identifier, "number", value)
        val services = createMockServices()

        // Act
        val result = variableDeclarationStrategy.visit(services, varDecl)

        // Assert
        assertTrue(result is Services)
        val newServices = result as Services
        assertTrue(newServices.context.containsKey("edad"))
        assertEquals(25.0, newServices.context["edad"])
    }

    @Test
    fun variableDeclarationStrategy_should_declare_variable_without_initial_value() {
        // Arrange
        val identifier = IdentifierNode("sinValor")
        val varDecl = VariableDeclarationNode(identifier, "string", null) // sin valor inicial
        val services = createMockServices()

        // Act
        val result = variableDeclarationStrategy.visit(services, varDecl)

        // Assert
        assertTrue(result is Services)
        val newServices = result as Services
        assertTrue(newServices.context.containsKey("sinValor"))
        assertNull(newServices.context["sinValor"]) // valor null
    }

    @Test
    fun variableDeclarationStrategy_should_preserve_existing_context() {
        // Arrange
        val existingContext = mapOf("existente" to "valor")
        val identifier = IdentifierNode("nueva")
        val value =
            LiteralNode(
                "\"nuevo\"",
                object : org.example.TokenType {
                    override val name = "STRING"
                },
            )
        val varDecl = VariableDeclarationNode(identifier, "string", value)
        val services = createMockServices(existingContext)

        // Act
        val result = variableDeclarationStrategy.visit(services, varDecl)

        // Assert
        assertTrue(result is Services)
        val newServices = result as Services

        // Debe tener ambas variables
        assertTrue(newServices.context.containsKey("existente"))
        assertTrue(newServices.context.containsKey("nueva"))
        assertEquals("valor", newServices.context["existente"])
        assertEquals("nuevo", newServices.context["nueva"])
    }

    @Test
    fun variableDeclarationStrategy_should_throw_exception_if_variable_already_exists() {
        // Arrange
        val existingContext = mapOf("duplicada" to "valor")
        val identifier = IdentifierNode("duplicada") // mismo nombre
        val value =
            LiteralNode(
                "\"nuevo\"",
                object : org.example.TokenType {
                    override val name = "STRING"
                },
            )
        val varDecl = VariableDeclarationNode(identifier, "string", value)
        val services = createMockServices(existingContext)

        // Act & Assert
        assertFailsWith<RuntimeException> {
            variableDeclarationStrategy.visit(services, varDecl)
        }
    }

    @Test
    fun variableDeclarationStrategy_should_handle_multiple_declarations() {
        // Arrange
        val services1 = createMockServices()

        // Primera declaración
        val identifier1 = IdentifierNode("var1")
        val value1 =
            LiteralNode(
                "\"first\"",
                object : org.example.TokenType {
                    override val name = "STRING"
                },
            )
        val varDecl1 = VariableDeclarationNode(identifier1, "string", value1)

        // Segunda declaración
        val identifier2 = IdentifierNode("var2")
        val value2 =
            LiteralNode(
                "42",
                object : org.example.TokenType {
                    override val name = "NUMBER"
                },
            )
        val varDecl2 = VariableDeclarationNode(identifier2, "number", value2)

        // Act
        val result1 = variableDeclarationStrategy.visit(services1, varDecl1) as Services
        val result2 = variableDeclarationStrategy.visit(result1, varDecl2) as Services

        // Assert
        assertTrue(result2.context.containsKey("var1"))
        assertTrue(result2.context.containsKey("var2"))
        assertEquals("first", result2.context["var1"])
        assertEquals(42.0, result2.context["var2"])
        assertEquals(2, result2.context.size)
    }
}
