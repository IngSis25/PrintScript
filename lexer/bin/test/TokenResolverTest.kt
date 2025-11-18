package test.lexerTest

import main.kotlin.lexer.TokenResolver
import org.example.Lexer.Location
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TokenResolverTest {
    @Test
    fun testGetTokenLet() {
        val resolver = TokenResolver(listOf("let" to "DeclarationToken"))
        val token = resolver.getToken("let", Location(1, 1))
        assertEquals("DeclarationToken", token.type)
        assertEquals("let", token.value)
    }

    @Test
    fun testGetTokenNumber() {
        val resolver = TokenResolver(listOf("[0-9]+" to "NumberToken"))
        val token = resolver.getToken("42", Location(1, 1))
        assertEquals("NumberToken", token.type)
        assertEquals("42", token.value)
    }

    @Test
    fun testGetTokenStringWithDoubleQuotes() {
        val resolver = TokenResolver(listOf("\".*\"" to "StringToken"))
        val token = resolver.getToken("\"hello\"", Location(1, 1))
        assertEquals("StringToken", token.type)
        assertEquals("hello", token.value)
    }

    @Test
    fun testGetTokenStringWithSingleQuotes() {
        val resolver = TokenResolver(listOf("\'.*\'" to "StringToken"))
        val token = resolver.getToken("'hello'", Location(1, 1))
        assertEquals("StringToken", token.type)
        assertEquals("hello", token.value)
    }

    @Test
    fun testGetTokenIdentifier() {
        val resolver = TokenResolver(listOf("[a-zA-Z_][a-zA-Z0-9_]*" to "IdentifierToken"))
        val token = resolver.getToken("myVariable", Location(1, 1))
        assertEquals("IdentifierToken", token.type)
        assertEquals("myVariable", token.value)
    }

    @Test
    fun testGetTokenOperator() {
        val resolver = TokenResolver(listOf("\\+" to "PlusToken"))
        val token = resolver.getToken("+", Location(1, 1))
        assertEquals("PlusToken", token.type)
        assertEquals("+", token.value)
    }

    @Test
    fun testGetTokenUnknownThrowsException() {
        val resolver = TokenResolver(listOf("let" to "DeclarationToken"))
        val location = Location(1, 1)
        val exception =
            assertThrows(Exception::class.java) {
                resolver.getToken("unknown", location)
            }
        assertTrue(exception.message?.contains("Lexicon Error") == true)
        assertTrue(exception.message?.contains("unknown") == true)
    }

    @Test
    fun testGetTokenFirstMatch() {
        // Si hay múltiples patrones que coinciden, debería tomar el primero
        val resolver =
            TokenResolver(
                listOf(
                    "let" to "DeclarationToken",
                    "[a-zA-Z_][a-zA-Z0-9_]*" to "IdentifierToken",
                ),
            )
        val token = resolver.getToken("let", Location(1, 1))
        assertEquals("DeclarationToken", token.type)
    }

    @Test
    fun testGetTokenLocation() {
        val resolver = TokenResolver(listOf("let" to "DeclarationToken"))
        val location = Location(5, 10)
        val token = resolver.getToken("let", location)
        assertEquals(location, token.location)
    }

    @Test
    fun testGetTokenFloatNumber() {
        val resolver = TokenResolver(listOf("[0-9]+\\.[0-9]+" to "NumberToken"))
        val token = resolver.getToken("3.14", Location(1, 1))
        assertEquals("NumberToken", token.type)
        assertEquals("3.14", token.value)
    }

    @Test
    fun testGetTokenRemovesQuotesFromString() {
        val resolver = TokenResolver(listOf("\".*\"" to "StringToken"))
        val token = resolver.getToken("\"test\"", Location(1, 1))
        assertEquals("test", token.value)
        // Verificar que las comillas fueron removidas
        assertFalse(token.value.contains("\""))
    }

    @Test
    fun testGetTokenRemovesSingleQuotesFromString() {
        val resolver = TokenResolver(listOf("\'.*\'" to "StringToken"))
        val token = resolver.getToken("'test'", Location(1, 1))
        assertEquals("test", token.value)
        // Verificar que las comillas simples fueron removidas
        assertFalse(token.value.contains("'"))
    }
}
