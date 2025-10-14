package test.parserTest

import ConfiguredTokens
import factory.ParserFactoryV11
import main.kotlin.lexer.DefaultLexer
import org.junit.jupiter.api.Test

class IfRuleTest {
    @Test
    fun testIfRuleMatching() {
        val input = "if (true) { println(\"hello\"); }"
        println("Testing: $input")

        val lexer = DefaultLexer(ConfiguredTokens.providerV11())
        val tokens = lexer.tokenize(input)

        println("Tokens generated:")
        tokens.forEachIndexed { index, token ->
            println("  [$index] ${token.type.name}: '${token.value}' at ${token.line}:${token.column}")
        }

        // Verificar que tenemos los tokens esperados
        assert(tokens.isNotEmpty()) { "No tokens generated" }
        assert(tokens[0].type.name == "IF") { "First token should be IF, got ${tokens[0].type.name}" }

        val parser = ParserFactoryV11().create()
        try {
            val ast = parser.parse(tokens)
            println("Success! AST size: ${ast.size}")
            ast.forEach { node ->
                println("  Node: ${node::class.simpleName}")
            }
        } catch (e: Exception) {
            println("Parser failed: ${e.message}")
            e.printStackTrace()
        }
    }
}
