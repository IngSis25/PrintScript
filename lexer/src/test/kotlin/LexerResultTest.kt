package test.lexerTest

import main.kotlin.lexer.LexerResult
import org.example.Lexer.Location
import org.example.Lexer.Token
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class LexerResultTest {
    @Test
    fun testLexerResultCreation() {
        val result = LexerResult()
        assertTrue(result.errors.isEmpty())
        assertTrue(result.tokens.isEmpty())
        assertFalse(result.hasErrors())
    }

    @Test
    fun testAddError() {
        val result = LexerResult()
        result.addError("Error 1")
        assertEquals(1, result.errors.size)
        assertEquals("Error 1", result.errors[0])
        assertTrue(result.hasErrors())
    }

    @Test
    fun testAddMultipleErrors() {
        val result = LexerResult()
        result.addError("Error 1")
        result.addError("Error 2")
        result.addError("Error 3")
        assertEquals(3, result.errors.size)
        assertEquals("Error 1", result.errors[0])
        assertEquals("Error 2", result.errors[1])
        assertEquals("Error 3", result.errors[2])
        assertTrue(result.hasErrors())
    }

    @Test
    fun testAddToken() {
        val result = LexerResult()
        val token = Token("TestToken", "value", Location(1, 1))
        result.addToken(token)
        assertEquals(1, result.tokens.size)
        assertEquals(token, result.tokens[0])
    }

    @Test
    fun testAddMultipleTokens() {
        val result = LexerResult()
        val token1 = Token("Token1", "value1", Location(1, 1))
        val token2 = Token("Token2", "value2", Location(1, 5))
        val token3 = Token("Token3", "value3", Location(1, 10))
        result.addToken(token1)
        result.addToken(token2)
        result.addToken(token3)
        assertEquals(3, result.tokens.size)
        assertEquals(token1, result.tokens[0])
        assertEquals(token2, result.tokens[1])
        assertEquals(token3, result.tokens[2])
    }

    @Test
    fun testHasErrorsWithErrors() {
        val result = LexerResult()
        result.addError("Error")
        assertTrue(result.hasErrors())
    }

    @Test
    fun testHasErrorsWithoutErrors() {
        val result = LexerResult()
        assertFalse(result.hasErrors())
    }

    @Test
    fun testMixedErrorsAndTokens() {
        val result = LexerResult()
        val token = Token("TestToken", "value", Location(1, 1))
        result.addToken(token)
        result.addError("Error")
        assertEquals(1, result.tokens.size)
        assertEquals(1, result.errors.size)
        assertTrue(result.hasErrors())
    }
}
