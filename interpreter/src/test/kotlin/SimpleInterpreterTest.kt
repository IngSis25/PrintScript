package test.interpreterTest

import ConfiguredTokens
import factory.ParserFactoryV11
import main.kotlin.lexer.DefaultLexer
import org.example.DefaultInterpreter
import org.example.output.Output
import org.example.strategy.PreConfiguredProviders
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

class SimpleInterpreterTest {
    private fun createTestOutput(): Pair<Output, ByteArrayOutputStream> {
        val outputStream = ByteArrayOutputStream()
        val output =
            object : Output {
                override fun write(msg: String) {
                    outputStream.write(msg.toByteArray())
                    print(msg) // También imprimir en consola para debug
                }
            }
        return output to outputStream
    }

    @Test
    fun debugSimpleIfStatement() {
        val input = "if (true) { println(\"hello\"); }"
        println("Testing: $input")

        val lexer = DefaultLexer(ConfiguredTokens.providerV11())
        val tokens = lexer.tokenize(input)
        println("Tokens: ${tokens.size}")

        val parser = ParserFactoryV11().create()
        val ast = parser.parse(tokens)
        println("AST nodes: ${ast.size}")

        val (output, outputStream) = createTestOutput()
        val interpreter = DefaultInterpreter(output, PreConfiguredProviders.VERSION_1_1)

        try {
            ast.forEach { node ->
                println("Interpreting node: ${node::class.simpleName}")
                interpreter.interpret(node)
            }

            val result = outputStream.toString()
            println("Output: '$result'")
            println("Expected: 'hello\\n'")
        } catch (e: Exception) {
            println("Error: ${e.message}")
            e.printStackTrace()
        }
    }
}
