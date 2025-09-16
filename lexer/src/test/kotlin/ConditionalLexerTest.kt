package test.lexerTest

import ConfiguredTokens
import main.kotlin.lexer.DefaultLexer
import main.kotlin.lexer.TokenProvider
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import types.*

class ConditionalLexerTest {
    @Test
    fun lexerShouldRecognizeIfKeyword() {
        val input = "if"
        val tokenProvider =
            TokenProvider.fromMap(
                mapOf("\\bif\\b" to IfType),
            )
        val lexer = DefaultLexer(tokenProvider)
        val tokens = lexer.tokenize(input)

        assertEquals(1, tokens.size)
        assertEquals("if", tokens[0].value)
        assertEquals(IfType, tokens[0].type)
        assertEquals("IF", tokens[0].type.name)
    }

    @Test
    fun lexerShouldRecognizeElseKeyword() {
        val input = "else"
        val tokenProvider =
            TokenProvider.fromMap(
                mapOf("\\belse\\b" to ElseType),
            )
        val lexer = DefaultLexer(tokenProvider)
        val tokens = lexer.tokenize(input)

        assertEquals(1, tokens.size)
        assertEquals("else", tokens[0].value)
        assertEquals(ElseType, tokens[0].type)
        assertEquals("ELSE_KEYWORD", tokens[0].type.name)
    }

    @Test
    fun lexerShouldRecognizeBooleanType() {
        val input = "Boolean"
        val tokenProvider =
            TokenProvider.fromMap(
                mapOf("\\bBoolean\\b" to BooleanType),
            )
        val lexer = DefaultLexer(tokenProvider)
        val tokens = lexer.tokenize(input)

        assertEquals(1, tokens.size)
        assertEquals("Boolean", tokens[0].value)
        assertEquals(BooleanType, tokens[0].type)
        assertEquals("BOOLEAN", tokens[0].type.name)
    }

    @Test
    fun lexerShouldRecognizeTrueLiteral() {
        val input = "true"
        val tokenProvider =
            TokenProvider.fromMap(
                mapOf("\\btrue\\b|\\bfalse\\b" to LiteralBoolean),
            )
        val lexer = DefaultLexer(tokenProvider)
        val tokens = lexer.tokenize(input)

        assertEquals(1, tokens.size)
        assertEquals("true", tokens[0].value)
        assertEquals(LiteralBoolean, tokens[0].type)
        assertEquals("LITERAL_BOOLEAN", tokens[0].type.name)
    }

    @Test
    fun lexerShouldRecognizeFalseLiteral() {
        val input = "false"
        val tokenProvider =
            TokenProvider.fromMap(
                mapOf("\\btrue\\b|\\bfalse\\b" to LiteralBoolean),
            )
        val lexer = DefaultLexer(tokenProvider)
        val tokens = lexer.tokenize(input)

        assertEquals(1, tokens.size)
        assertEquals("false", tokens[0].value)
        assertEquals(LiteralBoolean, tokens[0].type)
        assertEquals("LITERAL_BOOLEAN", tokens[0].type.name)
    }

    @Test
    fun lexerShouldRecognizeCurlyBraces() {
        val input = "{ }"
        val tokenProvider =
            TokenProvider.fromMap(
                mapOf(
                    "\\{" to PunctuationType,
                    "\\}" to PunctuationType,
                    "\\s+" to WhitespaceType,
                ),
            )
        val lexer = DefaultLexer(tokenProvider)
        val tokens = lexer.tokenize(input)

        // Should recognize { and } tokens (whitespace should be ignored if configured)
        assertTrue(tokens.size >= 2)
        assertEquals("{", tokens[0].value)
        assertEquals(PunctuationType, tokens[0].type)
        assertEquals("}", tokens[tokens.size - 1].value)
        assertEquals(PunctuationType, tokens[tokens.size - 1].type)
    }

    @Test
    fun lexerShouldTokenizeSimpleIfStatement() {
        val input = "if (condition) { }"
        val tokenProvider = ConfiguredTokens.providerV11()
        val lexer = DefaultLexer(tokenProvider)
        val tokens = lexer.tokenize(input)

        // Filter out whitespace tokens for easier testing
        val nonWhitespaceTokens = tokens.filter { !it.type.name.contains("WHITESPACE") }

        assertTrue(nonWhitespaceTokens.size >= 5)
        assertEquals("if", nonWhitespaceTokens[0].value)
        assertEquals(IfType, nonWhitespaceTokens[0].type)
        assertEquals("(", nonWhitespaceTokens[1].value)
        assertEquals(PunctuationType, nonWhitespaceTokens[1].type)
        assertEquals("condition", nonWhitespaceTokens[2].value)
        assertEquals(IdentifierType, nonWhitespaceTokens[2].type)
        assertEquals(")", nonWhitespaceTokens[3].value)
        assertEquals(PunctuationType, nonWhitespaceTokens[3].type)
        assertEquals("{", nonWhitespaceTokens[4].value)
        assertEquals(PunctuationType, nonWhitespaceTokens[4].type)
        assertEquals("}", nonWhitespaceTokens[5].value)
        assertEquals(PunctuationType, nonWhitespaceTokens[5].type)
    }

    @Test
    fun lexerShouldTokenizeIfElseStatement() {
        val input = "if (isValid) { println(\"true\"); } else { println(\"false\"); }"
        val tokenProvider = ConfiguredTokens.providerV11()
        val lexer = DefaultLexer(tokenProvider)
        val tokens = lexer.tokenize(input)

        // Filter out whitespace tokens
        val nonWhitespaceTokens = tokens.filter { !it.type.name.contains("WHITESPACE") }

        // Should contain if, (, identifier, ), {, println, (, string, ), ;, }, else, {, println, (, string, ), ;, }
        assertTrue(nonWhitespaceTokens.size >= 18)

        // Check key tokens
        assertEquals("if", nonWhitespaceTokens[0].value)
        assertEquals(IfType, nonWhitespaceTokens[0].type)

        // Find else token
        val elseTokenIndex = nonWhitespaceTokens.indexOfFirst { it.value == "else" }
        assertTrue(elseTokenIndex > 0)
        assertEquals(ElseType, nonWhitespaceTokens[elseTokenIndex].type)
    }

    @Test
    fun lexerShouldTokenizeBooleanVariableDeclaration() {
        val input = "let isActive: Boolean = true;"
        val tokenProvider = ConfiguredTokens.providerV11()
        val lexer = DefaultLexer(tokenProvider)
        val tokens = lexer.tokenize(input)

        // Filter out whitespace tokens
        val nonWhitespaceTokens = tokens.filter { !it.type.name.contains("WHITESPACE") }

        assertEquals("let", nonWhitespaceTokens[0].value)
        assertEquals("isActive", nonWhitespaceTokens[1].value)
        assertEquals(IdentifierType, nonWhitespaceTokens[1].type)
        assertEquals(":", nonWhitespaceTokens[2].value)
        assertEquals("Boolean", nonWhitespaceTokens[3].value)
        assertEquals(BooleanType, nonWhitespaceTokens[3].type)
        assertEquals("=", nonWhitespaceTokens[4].value)
        assertEquals("true", nonWhitespaceTokens[5].value)
        assertEquals(LiteralBoolean, nonWhitespaceTokens[5].type)
        assertEquals(";", nonWhitespaceTokens[6].value)
    }
}
