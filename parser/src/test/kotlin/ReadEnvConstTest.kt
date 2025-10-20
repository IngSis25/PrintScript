package test.parserTest

import main.kotlin.lexer.LexerFactoryV11
import factory.ParserFactoryV11
import org.example.DefaultInterpreter
import org.example.output.Output
import org.example.strategy.PreConfiguredProviders
import kotlin.test.*

class ReadEnvConstTest {
    private val lexer = LexerFactoryV11().create()
    private val parser = ParserFactoryV11().create()
    private val output = object : Output {
        val messages = mutableListOf<String>()
        override fun write(msg: String) {
            messages.add(msg)
        }
    }
    private val interpreter = DefaultInterpreter(output, PreConfiguredProviders.VERSION_1_1)

    @Test
    fun should_return_env() {
        // Set up environment variable
        val originalValue = System.getenv("BEST_FOOTBALL_CLUB")
        System.setProperty("BEST_FOOTBALL_CLUB", "San Lorenzo")
        
        try {
            val code = """
                const name: string = readEnv("BEST_FOOTBALL_CLUB");
                println("What is the best football club?");
                println(name);
            """.trimIndent()

            // Tokenize
            val tokens = lexer.tokenize(code)
            assertNotNull(tokens)
            assertTrue(tokens.isNotEmpty())

            // Parse
            val ast = parser.parse(tokens)
            assertNotNull(ast)
            assertTrue(ast.isNotEmpty())

            // Interpret
            ast.forEach { node ->
                interpreter.interpret(node)
            }

            // Verify output
            assertEquals(2, output.messages.size)
            assertEquals("What is the best football club?", output.messages[0])
            assertEquals("San Lorenzo", output.messages[1])
        } finally {
            // Clean up
            if (originalValue != null) {
                System.setProperty("BEST_FOOTBALL_CLUB", originalValue)
            } else {
                System.clearProperty("BEST_FOOTBALL_CLUB")
            }
        }
    }
}