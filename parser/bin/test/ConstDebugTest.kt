package test.parserTest

import factory.LexerFactoryV11
import factory.ParserFactoryV11
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class ConstDebugTest {
    @Test
    fun testConstBooleanDeclaration() {
        // Exactamente el mismo código que falla en el TCK
        val code = "const booleanResult: boolean = true;"

        val lexer = LexerFactoryV11().create()
        val parser = ParserFactoryV11().create()

        println("=== DEBUGGING CONST PARSING ===")
        println("Code: $code")

        // Tokenizar
        val tokens = lexer.tokenize(code)
        println("Tokens:")
        tokens.forEachIndexed { index, token ->
            println("  [$index] ${token.type} = '${token.value}'")
        }

        // Intentar parsear
        try {
            val parsed = parser.parse(tokens)
            println("Parsing SUCCESS!")
            println("Parsed nodes: ${parsed.size}")
            parsed.forEach { node ->
                println("  Node: $node")
            }
            assertNotNull(parsed)
        } catch (e: Exception) {
            println("Parsing FAILED: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    @Test
    fun testConstStringDeclaration() {
        // Otro caso del TCK
        val code = "const name: string = \"test\";"

        val lexer = LexerFactoryV11().create()
        val parser = ParserFactoryV11().create()

        println("=== DEBUGGING CONST STRING PARSING ===")
        println("Code: $code")

        val tokens = lexer.tokenize(code)
        println("Tokens:")
        tokens.forEachIndexed { index, token ->
            println("  [$index] ${token.type} = '${token.value}'")
        }

        try {
            val parsed = parser.parse(tokens)
            println("Parsing SUCCESS!")
            assertNotNull(parsed)
        } catch (e: Exception) {
            println("Parsing FAILED: ${e.message}")
            throw e
        }
    }
}
