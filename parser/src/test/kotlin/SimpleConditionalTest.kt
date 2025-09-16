package test.parserTest

import ConfiguredTokens
import factory.ParserFactoryV11
import main.kotlin.lexer.DefaultLexer
import org.junit.jupiter.api.Test

class SimpleConditionalTest {
    @Test
    fun debugSimpleIfStatement() {
        val input = "if (true) { println(\"hello\"); }"
        println("Input: $input")

        val lexer = DefaultLexer(ConfiguredTokens.providerV11())
        val tokens = lexer.tokenize(input)

        println("Tokens:")
        tokens.forEach { token ->
            println("  ${token.type.name}: '${token.value}' at ${token.line}:${token.column}")
        }

        val parser = ParserFactoryV11().create()
        try {
            val ast = parser.parse(tokens)
            println("AST size: ${ast.size}")
            ast.forEach { node ->
                println("  Node: ${node::class.simpleName}")
            }
        } catch (e: Exception) {
            println("Parser error: ${e.message}")
            e.printStackTrace()
        }
    }

    @Test
    fun debugBooleanLiteralParsing() {
        val input = "true"
        println("Input: $input")

        val lexer = DefaultLexer(ConfiguredTokens.providerV11())
        val tokens = lexer.tokenize(input)

        println("Tokens:")
        tokens.forEach { token ->
            println("  ${token.type.name}: '${token.value}' at ${token.line}:${token.column}")
        }

        val parser = ParserFactoryV11().create()
        try {
            val ast = parser.parse(tokens)
            println("AST size: ${ast.size}")
            ast.forEach { node ->
                println("  Node: ${node::class.simpleName}")
            }
        } catch (e: Exception) {
            println("Parser error: ${e.message}")
            e.printStackTrace()
        }
    }
}
