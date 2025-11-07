package test.parserTest

import factory.LexerFactoryV11
import main.kotlin.lexer.Token
import main.kotlin.parser.DefaultParser
import main.kotlin.parser.RuleMatcher
import org.example.DefaultInterpreter
import org.example.Output
import org.example.strategy.PreConfiguredProviders
import org.junit.jupiter.api.Test
import rules.productionRules

class MeLaComoDobladaTest {

    @Test
    fun meLaComoDoblada() {
        val code = "const booleanResult: boolean = true;\n" +
            "if(booleanResult) {\n" +
            "    println(\"else statement working correctly\");\n" +
            "} else {\n" +
            "    println(\"else statement not working correctly\");\n" +
            "}\n" +
            "println(\"outside of conditional\");\n"
        
        println("=== TESTING FULL CONST + IF FLOW ===")
        println("Code:")
        println(code)
        
        val lexer = LexerFactoryV11().create()
        val rules = productionRules()
        val matcher = RuleMatcher(rules)
        val parser = DefaultParser(matcher)
        val output = object : Output {
            override fun write(msg: String) {
                println("OUTPUT: $msg")
            }
        }
        val provider = PreConfiguredProviders.VERSION_1_1
        val interpreter = DefaultInterpreter(output, provider)
        
        println("\n--- TOKENIZING ---")
        val tokenized = lexer.tokenize(code)
        tokenized.forEachIndexed { index, token: Token ->
            println("  [$index] ${token.type} = '${token.value}'")
        }
        
        println("\n--- PARSING ---")
        val parsed = parser.parse(tokenized)
        println("Parsed ${parsed.size} AST nodes:")
        parsed.forEachIndexed { index, node ->
            println("  [$index] ${node::class.simpleName}: $node")
        }
        
        println("\n--- INTERPRETING ---")
        parsed.forEach {
            interpreter.interpret(it)
        }
        
        println("\n=== TEST COMPLETED ===")
    }
}
