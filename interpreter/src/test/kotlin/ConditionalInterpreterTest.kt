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

class ConditionalInterpreterTest {
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

    @Test
    fun interpreterShouldExecuteIfWithTrueCondition() {
        val input =
            """
            if (true) {
                println("executed");
            }
            """.trimIndent()

        val lexer = DefaultLexer(ConfiguredTokens.providerV11())
        val tokens = lexer.tokenize(input)

        val parser = ParserFactoryV11().create()
        val ast = parser.parse(tokens)

        val (output, outputStream) = createTestOutput()
        val interpreter = DefaultInterpreter(output, PreConfiguredProviders.VERSION_1_1)

        ast.forEach { node ->
            interpreter.interpret(node)
        }

        val result = outputStream.toString()
        assertEquals("executed\n", result)
    }

    @Test
    fun interpreterShouldSkipIfWithFalseCondition() {
        val input =
            """
            if (false) {
                println("should not execute");
            }
            """.trimIndent()

        val lexer = DefaultLexer(ConfiguredTokens.providerV11())
        val tokens = lexer.tokenize(input)

        val parser = ParserFactoryV11().create()
        val ast = parser.parse(tokens)

        val (output, outputStream) = createTestOutput()
        val interpreter = DefaultInterpreter(output, PreConfiguredProviders.VERSION_1_1)

        ast.forEach { node ->
            interpreter.interpret(node)
        }

        val result = outputStream.toString()
        assertEquals("", result)
    }

    @Test
    fun interpreterShouldExecuteElseWhenConditionIsFalse() {
        val input =
            """
            if (false) {
                println("if branch");
            } else {
                println("else branch");
            }
            """.trimIndent()

        val lexer = DefaultLexer(ConfiguredTokens.providerV11())
        val tokens = lexer.tokenize(input)

        val parser = ParserFactoryV11().create()
        val ast = parser.parse(tokens)

        val (output, outputStream) = createTestOutput()
        val interpreter = DefaultInterpreter(output, PreConfiguredProviders.VERSION_1_1)

        ast.forEach { node ->
            interpreter.interpret(node)
        }

        val result = outputStream.toString()
        assertEquals("else branch\n", result)
    }

    @Test
    fun interpreterShouldExecuteIfWhenConditionIsTrue() {
        val input =
            """
            if (true) {
                println("if branch");
            } else {
                println("else branch");
            }
            """.trimIndent()

        val lexer = DefaultLexer(ConfiguredTokens.providerV11())
        val tokens = lexer.tokenize(input)

        val parser = ParserFactoryV11().create()
        val ast = parser.parse(tokens)

        val (output, outputStream) = createTestOutput()
        val interpreter = DefaultInterpreter(output, PreConfiguredProviders.VERSION_1_1)

        ast.forEach { node ->
            interpreter.interpret(node)
        }

        val result = outputStream.toString()
        assertEquals("if branch\n", result)
    }

/*    @Test
    fun interpreterShouldWorkWithBooleanVariables() {
        val input = """
            let isActive: Boolean = true;
            println(isActive);
        """.trimIndent()

        val lexer = DefaultLexer(ConfiguredTokens.providerV11())
        val tokens = lexer.tokenize(input)

        val parser = ParserFactoryV11().create()
        val ast = parser.parse(tokens)

        val (output, outputStream) = createTestOutput()
        val interpreter = DefaultInterpreter(output, PreConfiguredProviders.VERSION_1_1)

        ast.forEach { node ->
            interpreter.interpret(node)
        }

        val result = outputStream.toString()
        assertEquals("true\n", result)
    }*/

    @Test
    fun interpreterShouldThrowErrorForNonBooleanCondition() {
        val input =
            """
            let value: number = 42;
            if (value) {
                println("should not reach here");
            }
            """.trimIndent()

        val lexer = DefaultLexer(ConfiguredTokens.providerV11())
        val tokens = lexer.tokenize(input)

        val parser = ParserFactoryV11().create()
        val ast = parser.parse(tokens)

        val (output, _) = createTestOutput()
        val interpreter = DefaultInterpreter(output, PreConfiguredProviders.VERSION_1_1)

        assertThrows(IllegalArgumentException::class.java) {
            ast.forEach { node ->
                interpreter.interpret(node)
            }
        }
    }

    @Test
    fun interpreterShouldThrowErrorForUndefinedVariable() {
        val input =
            """
            if (undefinedVar) {
                println("should not reach here");
            }
            """.trimIndent()

        val lexer = DefaultLexer(ConfiguredTokens.providerV11())
        val tokens = lexer.tokenize(input)

        val parser = ParserFactoryV11().create()
        val ast = parser.parse(tokens)

        val (output, _) = createTestOutput()
        val interpreter = DefaultInterpreter(output, PreConfiguredProviders.VERSION_1_1)

        assertThrows(IllegalArgumentException::class.java) {
            ast.forEach { node ->
                interpreter.interpret(node)
            }
        }
    }
}
