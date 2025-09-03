import lexer.TokenRule
import main.kotlin.lexer.TokenProvider
import org.junit.jupiter.api.Assertions.assertEquals
import types.AssignmentType
import types.PunctuationType
import kotlin.test.Test

class MainTest {
    private fun makeProvider(): TokenProvider {
        val baseProvider = ConfiguredTokens.providerV1()
        val ignored =
            listOf(
                TokenRule(Regex("\\G[ \\t]+"), PunctuationType, ignore = true), // espacios/tabs
                TokenRule(Regex("\\G(?:\\r?\\n)+"), PunctuationType, ignore = true), // saltos de línea
                TokenRule(Regex("\\G//.*(?:\\r?\\n|$)"), PunctuationType, ignore = true), // comentarios //
            )
        return TokenProvider(ignored + baseProvider.rules())
    }

    @Test
    fun tokenize_x_eq_2_semicolon_positions_and_types() {
        val lexer = DefaultLexer(makeProvider())
        val tokens = lexer.tokenize("x = 2;")

        // 4 tokens: x, =, 2, ;
        assertEquals(4, tokens.size)

        assertEquals("x", tokens[0].value)
        assertEquals(main.kotlin.lexer.IdentifierType, tokens[0].type)
        assertEquals(1, tokens[0].line)
        assertEquals(1, tokens[0].column)

        assertEquals("=", tokens[1].value)
        assertEquals(AssignmentType, tokens[1].type)
        assertEquals(1, tokens[1].line)
        assertEquals(3, tokens[1].column) // el espacio ignorado avanza col

        assertEquals("2", tokens[2].value)
        assertEquals(main.kotlin.lexer.LiteralNumber, tokens[2].type)
        assertEquals(1, tokens[2].line)
        assertEquals(5, tokens[2].column)

        assertEquals(";", tokens[3].value)
        assertEquals(PunctuationType, tokens[3].type)
        assertEquals(1, tokens[3].line)
        assertEquals(6, tokens[3].column)
    }
}
