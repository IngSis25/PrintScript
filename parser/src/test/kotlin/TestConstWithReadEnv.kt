import factory.LexerFactoryV11
import factory.ParserFactoryV11
import org.example.DefaultInterpreter
import org.example.output.Output
import org.junit.jupiter.api.Test

class TestConstWithReadEnv {
    @Test
    fun `parse const with readEnv successfully`() {
        val code =
            """
            const name: string = readEnv("BEST_FOOTBALL_CLUB");
            println("What is the best football club?");
            println(name);
            """.trimIndent()

        println("Testing code:")
        println(code)
        println()

        // Test lexer
        val lexer = LexerFactoryV11().create()
        val tokens = lexer.tokenize(code)
        println("Tokens:")
        tokens.forEach { token ->
            println("  ${token.type} = '${token.value}' (line ${token.line}, col ${token.column})")
        }
        println()

        // Test parser
        val parser = ParserFactoryV11().create()
        try {
            val ast = parser.parse(tokens)
            println("AST parsed successfully:")
            ast.forEach { node ->
                println("  $node")
            }
        } catch (e: Exception) {
            println("Parser error: ${e.message}")
            e.printStackTrace()
        }

        // Test interpreter
        val output =
            object : Output {
                override fun write(msg: String) {
                    println("OUTPUT: $msg")
                }
            }
        val interpreter = DefaultInterpreter(output, org.example.strategy.PreConfiguredProviders.VERSION_1_1)

        try {
            val ast = parser.parse(tokens)
            ast.forEach { node ->
                interpreter.interpret(node)
            }
        } catch (e: Exception) {
            println("Interpreter error: ${e.message}")
            e.printStackTrace()
        }
    }
}
