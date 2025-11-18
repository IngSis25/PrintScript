package test.lexerTest

import main.kotlin.lexer.TokenFactory
import org.example.Lexer.Location
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TokenFactoryTest {
    @Test
    fun testCreateLexerV10() {
        val factory = TokenFactory()
        val resolver = factory.createLexerV10()
        assertNotNull(resolver)
        // Verificar que puede resolver tokens básicos
        val token = resolver.getToken("let", Location(1, 1))
        assertEquals("DeclarationToken", token.type)
    }

    @Test
    fun testCreateLexerV11() {
        val factory = TokenFactory()
        val resolver = factory.createLexerV11()
        assertNotNull(resolver)
        // Verificar que puede resolver tokens básicos
        val token = resolver.getToken("let", Location(1, 1))
        assertEquals("DeclarationToken", token.type)
    }

    @Test
    fun testCreateLexerV11HasConst() {
        val factory = TokenFactory()
        val resolver = factory.createLexerV11()
        // V11 debería tener "const"
        val token = resolver.getToken("const", Location(1, 1))
        assertEquals("DeclarationToken", token.type)
    }

    @Test
    fun testCreateLexerV10HasPrintln() {
        val factory = TokenFactory()
        val resolver = factory.createLexerV10()
        val token = resolver.getToken("println", Location(1, 1))
        assertEquals("PrintToken", token.type)
    }

    @Test
    fun testCreateLexerV11HasPrintln() {
        val factory = TokenFactory()
        val resolver = factory.createLexerV11()
        val token = resolver.getToken("println", Location(1, 1))
        assertEquals("PrintToken", token.type)
    }

    @Test
    fun testCreateLexerV11HasIf() {
        val factory = TokenFactory()
        val resolver = factory.createLexerV11()
        val token = resolver.getToken("if", Location(1, 1))
        assertEquals("IfToken", token.type)
    }

    @Test
    fun testCreateLexerV11HasElse() {
        val factory = TokenFactory()
        val resolver = factory.createLexerV11()
        val token = resolver.getToken("else", Location(1, 1))
        assertEquals("ElseToken", token.type)
    }

    @Test
    fun testCreateLexerV11HasReadInput() {
        val factory = TokenFactory()
        val resolver = factory.createLexerV11()
        val token = resolver.getToken("readInput", Location(1, 1))
        assertEquals("ReadInputToken", token.type)
    }

    @Test
    fun testCreateLexerV11HasReadEnv() {
        val factory = TokenFactory()
        val resolver = factory.createLexerV11()
        val token = resolver.getToken("readEnv", Location(1, 1))
        assertEquals("ReadEnvironmentToken", token.type)
    }

    @Test
    fun testCreateLexerV11HasBoolean() {
        val factory = TokenFactory()
        val resolver = factory.createLexerV11()
        val token = resolver.getToken("boolean", Location(1, 1))
        assertEquals("TypeToken", token.type)
    }

    @Test
    fun testCreateLexerV11HasTrue() {
        val factory = TokenFactory()
        val resolver = factory.createLexerV11()
        val token = resolver.getToken("true", Location(1, 1))
        assertEquals("BooleanToken", token.type)
    }

    @Test
    fun testCreateLexerV11HasFalse() {
        val factory = TokenFactory()
        val resolver = factory.createLexerV11()
        val token = resolver.getToken("false", Location(1, 1))
        assertEquals("BooleanToken", token.type)
    }

    @Test
    fun testCreateLexerV11HasBraces() {
        val factory = TokenFactory()
        val resolver = factory.createLexerV11()
        val openBrace = resolver.getToken("{", Location(1, 1))
        val closeBrace = resolver.getToken("}", Location(1, 1))
        assertEquals("OpenBraceToken", openBrace.type)
        assertEquals("CloseBraceToken", closeBrace.type)
    }

    @Test
    fun testCreateLexerV10DoesNotHaveBraces() {
        val factory = TokenFactory()
        val resolver = factory.createLexerV10()
        // V10 no debería tener llaves
        val exception =
            assertThrows(Exception::class.java) {
                resolver.getToken("{", Location(1, 1))
            }
        assertTrue(exception.message?.contains("Lexicon Error") == true)
    }
}
