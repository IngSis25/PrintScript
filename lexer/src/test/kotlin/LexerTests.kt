import main.kotlin.lexer.*
import org.example.LiteralNumber
import org.example.LiteralString
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import types.IdentifierType
import types.NumberType
import types.OperatorType
import types.PunctuationType
import types.StringType

class LexerTests {
    @Test
    fun lexerShouldRecognizeStringTypeToken() {
        val input = "String"
        val tokenProvider =
            TokenProvider.fromMap(
                mapOf("String" to StringType),
            )
        val lexer = DefaultLexer(tokenProvider)
        val tokens = lexer.tokenize(input)

        assertEquals(tokens.size, 1)
        assertEquals(tokens[0].value, "String")
        assertEquals(tokens[0].type, StringType)
    }

    @Test
    fun lexerShouldRecognizeNumberTypeToken() {
        val input = "Int"
        val tokenProvider =
            TokenProvider.fromMap(
                mapOf("Int" to NumberType),
            )
        val lexer = DefaultLexer(tokenProvider)
        val tokens = lexer.tokenize(input)

        assertEquals(tokens.size, 1)
        assertEquals(tokens[0].value, "Int")
        assertEquals(tokens[0].type, NumberType)
    }

    @Test
    fun lexerShouldRecognizeLiteralNumberToken() {
        val input = "123"
        val tokenProvider =
            TokenProvider.fromMap(
                mapOf("\\d+" to LiteralNumber),
            )
        val lexer = DefaultLexer(tokenProvider)
        val tokens = lexer.tokenize(input)

        assertEquals(tokens.size, 1)
        assertEquals(tokens[0].value, "123")
        assertEquals(tokens[0].type, LiteralNumber)
    }

    @Test
    fun lexerShouldRecognizeLiteralStringToken() {
        val input = "\"Olivia\""
        val tokenProvider =
            TokenProvider.fromMap(
                mapOf("\"[^\"]*\"" to LiteralString),
            )
        val lexer = DefaultLexer(tokenProvider)
        val tokens = lexer.tokenize(input)

        assertEquals(tokens.size, 1)
        assertEquals(tokens[0].value, "\"Olivia\"")
        assertEquals(tokens[0].type, LiteralString)
    }

    @Test
    fun lexerShouldRecognizePunctuationTypeToken() {
        val input = ";"
        val tokenProvider =
            TokenProvider.fromMap(
                mapOf(";" to PunctuationType),
            )
        val lexer = DefaultLexer(tokenProvider)
        val tokens = lexer.tokenize(input)

        assertEquals(tokens.size, 1)
        assertEquals(tokens[0].value, ";")
        assertEquals(tokens[0].type, PunctuationType)
    }

    @Test
    fun lexerShouldRecognizeIdentifierTypeToken() {
        val input = "variable1"
        val tokenProvider =
            TokenProvider.fromMap(
                mapOf("[a-zA-Z_][a-zA-Z0-9_]*" to IdentifierType),
            )
        val lexer = DefaultLexer(tokenProvider)
        val tokens = lexer.tokenize(input)

        assertEquals(tokens.size, 1)
        assertEquals(tokens[0].value, "variable1")
        assertEquals(tokens[0].type, IdentifierType)
    }

    @Test
    fun lexerShouldRecognizeOperatorTypeToken() {
        val input = "+"
        val tokenProvider =
            TokenProvider.fromMap(
                mapOf("\\+" to OperatorType),
            )
        val lexer = DefaultLexer(tokenProvider)
        val tokens = lexer.tokenize(input)

        assertEquals(tokens.size, 1)
        assertEquals(tokens[0].value, "+")
        assertEquals(tokens[0].type, OperatorType)
    }
}
