package test.integrationTest

import main.kotlin.lexer.LexerFactoryV11
import factory.ParserFactoryV11
import org.example.DefaultInterpreter
import org.example.output.Output
import org.example.strategy.PreConfiguredProviders
import kotlin.test.*

class ReadEnvIntegrationTests {
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
    fun should_parse_and_interpret_readenv_const_declaration() {
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

    @Test
    fun should_handle_missing_environment_variable() {
        val code = """
            const name: string = readEnv("NON_EXISTENT_VAR");
            println(name);
        """.trimIndent()

        val tokens = lexer.tokenize(code)
        val ast = parser.parse(tokens)

        assertFailsWith<RuntimeException> {
            ast.forEach { node ->
                interpreter.interpret(node)
            }
        }
    }

    @Test
    fun should_handle_multiple_readenv_calls() {
        // Set up environment variables
        val originalValue1 = System.getenv("CLUB_NAME")
        val originalValue2 = System.getenv("CLUB_CITY")
        System.setProperty("CLUB_NAME", "San Lorenzo")
        System.setProperty("CLUB_CITY", "Buenos Aires")
        
        try {
            val code = """
                const club: string = readEnv("CLUB_NAME");
                const city: string = readEnv("CLUB_CITY");
                println(club);
                println(city);
            """.trimIndent()

            val tokens = lexer.tokenize(code)
            val ast = parser.parse(tokens)

            ast.forEach { node ->
                interpreter.interpret(node)
            }

            // Verify output
            assertEquals(2, output.messages.size)
            assertEquals("San Lorenzo", output.messages[0])
            assertEquals("Buenos Aires", output.messages[1])
        } finally {
            // Clean up
            if (originalValue1 != null) {
                System.setProperty("CLUB_NAME", originalValue1)
            } else {
                System.clearProperty("CLUB_NAME")
            }
            if (originalValue2 != null) {
                System.setProperty("CLUB_CITY", originalValue2)
            } else {
                System.clearProperty("CLUB_CITY")
            }
        }
    }
}