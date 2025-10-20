package test.interpreterTest

import ast.ReadEnvNode
import org.example.strategy.readEnvStrategy
import org.example.util.Services
import kotlin.test.*

class ReadEnvStrategyTests {
    private val services = Services()

    @Test
    fun should_return_env_variable_value() {
        // Set up environment variable
        val originalValue = System.getenv("TEST_ENV_VAR")
        System.setProperty("TEST_ENV_VAR", "San Lorenzo")
        
        try {
            val readEnvNode = ReadEnvNode("TEST_ENV_VAR")
            val result = readEnvStrategy.interpret(services, readEnvNode)
            
            assertEquals("San Lorenzo", result)
        } finally {
            // Clean up
            if (originalValue != null) {
                System.setProperty("TEST_ENV_VAR", originalValue)
            } else {
                System.clearProperty("TEST_ENV_VAR")
            }
        }
    }

    @Test
    fun should_throw_exception_when_env_variable_not_found() {
        val readEnvNode = ReadEnvNode("NON_EXISTENT_ENV_VAR")
        
        assertFailsWith<RuntimeException> {
            readEnvStrategy.interpret(services, readEnvNode)
        }
    }

    @Test
    fun should_return_empty_string_for_empty_env_variable() {
        // Set up empty environment variable
        val originalValue = System.getenv("EMPTY_ENV_VAR")
        System.setProperty("EMPTY_ENV_VAR", "")
        
        try {
            val readEnvNode = ReadEnvNode("EMPTY_ENV_VAR")
            val result = readEnvStrategy.interpret(services, readEnvNode)
            
            assertEquals("", result)
        } finally {
            // Clean up
            if (originalValue != null) {
                System.setProperty("EMPTY_ENV_VAR", originalValue)
            } else {
                System.clearProperty("EMPTY_ENV_VAR")
            }
        }
    }
}