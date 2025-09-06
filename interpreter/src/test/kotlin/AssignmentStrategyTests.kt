package test.interpreterTest

import org.example.ast.AssignmentNode
import org.example.ast.IdentifierNode
import org.example.ast.LiteralNode
import org.example.output.Output
import org.example.strategy.assignmentStrategy
import org.example.strategy.literalStrategy
import org.example.util.Services
import kotlin.test.*

class AssignmentStrategyTests {
    private fun createMockServices(context: Map<String, Any?> = emptyMap()): Services {
        val mockOutput =
            object : Output {
                override infix fun write(msg: String) {}
            }
        return Services(
            context = context,
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
    fun assignmentStrategy_should_update_existing_string_variable() {
        // Arrange
        val existingContext = mapOf("nombre" to "valorAnterior")
        val identifier = IdentifierNode("nombre")
        val newValue =
            LiteralNode(
                "\"nuevoValor\"",
                object : org.example.TokenType {
                    override val name = "STRING"
                },
            )
        val assignment = AssignmentNode(identifier, newValue)
        val services = createMockServices(existingContext)

        // Act
        val result = assignmentStrategy.visit(services, assignment)

        // Assert
        assertTrue(result is Services)
        val newServices = result as Services
        assertEquals("nuevoValor", newServices.context["nombre"]) // sin comillas
    }

    @Test
    fun assignmentStrategy_should_update_existing_number_variable() {
        // Arrange
        val existingContext = mapOf("edad" to 25.0)
        val identifier = IdentifierNode("edad")
        val newValue =
            LiteralNode(
                "30",
                object : org.example.TokenType {
                    override val name = "NUMBER"
                },
            )
        val assignment = AssignmentNode(identifier, newValue)
        val services = createMockServices(existingContext)

        // Act
        val result = assignmentStrategy.visit(services, assignment)

        // Assert
        assertTrue(result is Services)
        val newServices = result as Services
        assertEquals(30.0, newServices.context["edad"])
    }

    @Test
    fun assignmentStrategy_should_preserve_other_variables() {
        // Arrange
        val existingContext =
            mapOf(
                "var1" to "valor1",
                "var2" to 42.0,
                "var3" to "valor3",
            )
        val identifier = IdentifierNode("var2")
        val newValue =
            LiteralNode(
                "100",
                object : org.example.TokenType {
                    override val name = "NUMBER"
                },
            )
        val assignment = AssignmentNode(identifier, newValue)
        val services = createMockServices(existingContext)

        // Act
        val result = assignmentStrategy.visit(services, assignment)

        // Assert
        assertTrue(result is Services)
        val newServices = result as Services

        // var2 debe estar actualizada
        assertEquals(100.0, newServices.context["var2"])

        // otras variables deben mantenerse igual
        assertEquals("valor1", newServices.context["var1"])
        assertEquals("valor3", newServices.context["var3"])
        assertEquals(3, newServices.context.size)
    }

    @Test
    fun assignmentStrategy_should_throw_exception_for_undeclared_variable() {
        // Arrange
        val existingContext = mapOf("existente" to "valor")
        val identifier = IdentifierNode("noExiste") // variable no declarada
        val newValue =
            LiteralNode(
                "\"valor\"",
                object : org.example.TokenType {
                    override val name = "STRING"
                },
            )
        val assignment = AssignmentNode(identifier, newValue)
        val services = createMockServices(existingContext)

        // Act & Assert
        val exception =
            assertFailsWith<RuntimeException> {
                assignmentStrategy.visit(services, assignment)
            }
        assertTrue(exception.message!!.contains("noExiste"))
        assertTrue(exception.message!!.contains("no declarada"))
    }

    @Test
    fun assignmentStrategy_should_handle_assignment_to_null_variable() {
        // Arrange
        val existingContext = mapOf("variable" to null)
        val identifier = IdentifierNode("variable")
        val newValue =
            LiteralNode(
                "\"ahora tiene valor\"",
                object : org.example.TokenType {
                    override val name = "STRING"
                },
            )
        val assignment = AssignmentNode(identifier, newValue)
        val services = createMockServices(existingContext)

        // Act
        val result = assignmentStrategy.visit(services, assignment)

        // Assert
        assertTrue(result is Services)
        val newServices = result as Services
        assertEquals("ahora tiene valor", newServices.context["variable"])
    }

    @Test
    fun assignmentStrategy_should_handle_assignment_to_null_value() {
        // Arrange
        val existingContext = mapOf("variable" to "valor anterior")
        val identifier = IdentifierNode("variable")
        val services =
            Services(
                context = existingContext,
                output =
                    object : Output {
                        override fun write(msg: String) {}
                    },
                visit = { _, _ -> null }, // devuelve null
            )
        val newValue =
            LiteralNode(
                "cualquier",
                object : org.example.TokenType {
                    override val name = "STRING"
                },
            )
        val assignment = AssignmentNode(identifier, newValue)

        // Act
        val result = assignmentStrategy.visit(services, assignment)

        // Assert
        assertTrue(result is Services)
        val newServices = result as Services
        assertNull(newServices.context["variable"]) // ahora es null
    }

    @Test
    fun assignmentStrategy_should_handle_empty_context() {
        // Arrange
        val identifier = IdentifierNode("cualquier")
        val newValue =
            LiteralNode(
                "\"valor\"",
                object : org.example.TokenType {
                    override val name = "STRING"
                },
            )
        val assignment = AssignmentNode(identifier, newValue)
        val services = createMockServices(emptyMap())

        // Act & Assert
        assertFailsWith<RuntimeException> {
            assignmentStrategy.visit(services, assignment)
        }
    }

    @Test
    fun assignmentStrategy_should_handle_sequential_assignments() {
        // Arrange
        val initialContext = mapOf("variable" to "inicial")
        val services1 = createMockServices(initialContext)

        val identifier = IdentifierNode("variable")
        val value1 =
            LiteralNode(
                "\"primer cambio\"",
                object : org.example.TokenType {
                    override val name = "STRING"
                },
            )
        val value2 =
            LiteralNode(
                "\"segundo cambio\"",
                object : org.example.TokenType {
                    override val name = "STRING"
                },
            )

        val assignment1 = AssignmentNode(identifier, value1)
        val assignment2 = AssignmentNode(identifier, value2)

        // Act
        val result1 = assignmentStrategy.visit(services1, assignment1) as Services
        val result2 = assignmentStrategy.visit(result1, assignment2) as Services

        // Assert
        assertEquals("segundo cambio", result2.context["variable"])
    }
}
