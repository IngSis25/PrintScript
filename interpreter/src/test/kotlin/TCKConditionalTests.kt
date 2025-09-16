package test.interpreterTest

import ConfiguredTokens
import factory.ParserFactoryV11
import main.kotlin.lexer.DefaultLexer
import org.example.DefaultInterpreter
import org.example.output.Output
import org.example.strategy.PreConfiguredProviders
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

/**
 * Tests específicos del TCK para expresiones condicionales
 * Simulan los casos: @else-statement-true/ @else-statement-false/
 */
class TCKConditionalTests {
    private fun createTestOutput(): Pair<Output, ByteArrayOutputStream> {
        val outputStream = ByteArrayOutputStream()
        val output =
            object : Output {
                override fun write(msg: String) {
                    outputStream.write(msg.toByteArray())
                }
            }
        return output to outputStream
    }

    private fun executeCode(code: String): String {
        val lexer = DefaultLexer(ConfiguredTokens.providerV11())
        val tokens = lexer.tokenize(code)

        val parser = ParserFactoryV11().create()
        val ast = parser.parse(tokens)

        val (output, outputStream) = createTestOutput()
        val interpreter = DefaultInterpreter(output, PreConfiguredProviders.VERSION_1_1)

        ast.forEach { node ->
            interpreter.interpret(node)
        }

        return outputStream.toString()
    }

    @Test
    fun `else-statement-true - Should execute else block when condition is false`() {
        val code =
            """
            if (false) {
                println("if block");
            } else {
                println("else block");
            }
            """.trimIndent()

        val result = executeCode(code)
        assertEquals("else block\n", result)
    }

    @Test
    fun `else-statement-false - Should not execute else block when condition is true`() {
        val code =
            """
            if (true) {
                println("if block");
            } else {
                println("else block");
            }
            """.trimIndent()

        val result = executeCode(code)
        assertEquals("if block\n", result)
    }

    @Test
    fun `else-statement-true with complex condition - Should execute else when condition evaluates to false`() {
        val code =
            """
            if (false) {
                println("should not print");
            } else {
                println("should print");
            }
            """.trimIndent()

        val result = executeCode(code)
        assertEquals("should print\n", result)
    }

    @Test
    fun `else-statement-false with complex condition - Should not execute else when condition evaluates to true`() {
        val code =
            """
            if (true) {
                println("should print");
            } else {
                println("should not print");
            }
            """.trimIndent()

        val result = executeCode(code)
        assertEquals("should print\n", result)
    }

    @Test
    fun `multiple statements in else block - true condition`() {
        val code =
            """
            if (false) {
                println("if1");
                println("if2");
            } else {
                println("else1");
                println("else2");
            }
            """.trimIndent()

        val result = executeCode(code)
        assertEquals("else1\nelse2\n", result)
    }

    @Test
    fun `multiple statements in else block - false condition`() {
        val code =
            """
            if (true) {
                println("if1");
                println("if2");
            } else {
                println("else1");
                println("else2");
            }
            """.trimIndent()

        val result = executeCode(code)
        assertEquals("if1\nif2\n", result)
    }

    @Test
    fun `if without else - condition true`() {
        val code =
            """
            if (true) {
                println("executed");
            }
            """.trimIndent()

        val result = executeCode(code)
        assertEquals("executed\n", result)
    }

    @Test
    fun `if without else - condition false`() {
        val code =
            """
            if (false) {
                println("not executed");
            }
            """.trimIndent()

        val result = executeCode(code)
        assertEquals("", result)
    }

    @Test
    fun `empty if block - condition true`() {
        val code =
            """
            if (true) {
            } else {
                println("else executed");
            }
            """.trimIndent()

        val result = executeCode(code)
        assertEquals("", result)
    }

    @Test
    fun `empty else block - condition false`() {
        val code =
            """
            if (false) {
                println("if executed");
            } else {
            }
            """.trimIndent()

        val result = executeCode(code)
        assertEquals("", result)
    }

    @Test
    fun `sequential if-else statements`() {
        val code =
            """
            if (true) {
                println("first if");
            } else {
                println("first else");
            }
            if (false) {
                println("second if");
            } else {
                println("second else");
            }
            """.trimIndent()

        val result = executeCode(code)
        assertEquals("first if\nsecond else\n", result)
    }
}
