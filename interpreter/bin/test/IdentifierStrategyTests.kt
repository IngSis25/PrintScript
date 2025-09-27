package test.interpreterTest

import org.example.ast.IdentifierNode
import org.example.output.Output
import org.example.strategy.identifierStrategy
import org.example.util.Services
import kotlin.test.*

class IdentifierStrategyTests {
    private fun createMockServices(context: Map<String, Any?> = emptyMap()): Services {
        val mockOutput =
            object : Output {
                override infix fun write(msg: String) {}
            }
        return Services(
            context = context,
            output = mockOutput,
            visit = { _, _ -> null },
        )
    }

    @Test
    fun identifierStrategy_should_return_string_variable_value() {
        // Arrange
        val context = mapOf("nombre" to "Juan")
        val identifier = IdentifierNode("nombre")
        val services = createMockServices(context)

        // Act
        val result = identifierStrategy.visit(services, identifier)

        // Assert
        assertEquals("Juan", result)
    }

    @Test
    fun identifierStrategy_should_return_number_variable_value() {
        // Arrange
        val context = mapOf("edad" to 25.0)
        val identifier = IdentifierNode("edad")
        val services = createMockServices(context)

        // Act
        val result = identifierStrategy.visit(services, identifier)

        // Assert
        assertEquals(25.0, result)
    }

    @Test
    fun identifierStrategy_should_return_null_variable_value() {
        // Arrange
        val context = mapOf("variable" to null)
        val identifier = IdentifierNode("variable")
        val services = createMockServices(context)

        // Act
        val result = identifierStrategy.visit(services, identifier)

        // Assert
        assertNull(result)
    }

    @Test
    fun identifierStrategy_should_throw_exception_for_undeclared_variable() {
        // Arrange
        val context = mapOf("existente" to "valor")
        val identifier = IdentifierNode("noExiste") // variable no declarada
        val services = createMockServices(context)

        // Act & Assert
        val exception =
            assertFailsWith<RuntimeException> {
                identifierStrategy.visit(services, identifier)
            }
        assertTrue(exception.message!!.contains("noExiste"))
        assertTrue(exception.message!!.contains("no declarada"))
    }

    @Test
    fun identifierStrategy_should_handle_empty_context() {
        // Arrange
        val identifier = IdentifierNode("cualquier")
        val services = createMockServices(emptyMap())

        // Act & Assert
        assertFailsWith<RuntimeException> {
            identifierStrategy.visit(services, identifier)
        }
    }

    @Test
    fun identifierStrategy_should_handle_multiple_variables_in_context() {
        // Arrange
        val context =
            mapOf(
                "var1" to "string",
                "var2" to 42.0,
                "var3" to true,
                "var4" to null,
            )
        val services = createMockServices(context)

        // Act & Assert - cada variable debería devolver su valor correcto
        assertEquals("string", identifierStrategy.visit(services, IdentifierNode("var1")))
        assertEquals(42.0, identifierStrategy.visit(services, IdentifierNode("var2")))
        assertEquals(true, identifierStrategy.visit(services, IdentifierNode("var3")))
        assertNull(identifierStrategy.visit(services, IdentifierNode("var4")))
    }

    @Test
    fun identifierStrategy_should_be_case_sensitive() {
        // Arrange
        val context = mapOf("Variable" to "valor")
        val services = createMockServices(context)

        // Act & Assert
        assertEquals("valor", identifierStrategy.visit(services, IdentifierNode("Variable")))

        // Diferente case debería fallar
        assertFailsWith<RuntimeException> {
            identifierStrategy.visit(services, IdentifierNode("variable")) // lowercase
        }
    }

    @Test
    fun identifierStrategy_should_handle_complex_variable_names() {
        // Arrange
        val context =
            mapOf(
                "var_with_underscore" to "test1",
                "varWithCamelCase" to "test2",
                "var123" to "test3",
            )
        val services = createMockServices(context)

        // Act & Assert
        assertEquals("test1", identifierStrategy.visit(services, IdentifierNode("var_with_underscore")))
        assertEquals("test2", identifierStrategy.visit(services, IdentifierNode("varWithCamelCase")))
        assertEquals("test3", identifierStrategy.visit(services, IdentifierNode("var123")))
    }
}
