package main.kotlin.lexer

import factory.LexerFactoryRegistry
import org.example.LiteralString
import org.junit.jupiter.api.Test
import types.PrintlnType
import types.PunctuationType
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PrintlnLexerTest {
    @Test
    fun testPrintlnWithString() {
        val factory = LexerFactoryRegistry.getFactory("1.0")
        val lexer = factory.create()

        val code = "println(\"This is a text\");"
        val tokens = lexer.tokenize(code)

        // Deberíamos tener 5 tokens: println, (, "This is a text", ), ;
        assertEquals(5, tokens.size)

        // Token 1: println
        assertEquals("println", tokens[0].value)
        assertTrue(tokens[0].type is PrintlnType)

        // Token 2: (
        assertEquals("(", tokens[1].value)
        assertTrue(tokens[1].type is PunctuationType)

        // Token 3: "This is a text"
        assertEquals("\"This is a text\"", tokens[2].value)
        assertTrue(tokens[2].type is LiteralString)

        // Token 4: )
        assertEquals(")", tokens[3].value)
        assertTrue(tokens[3].type is PunctuationType)

        // Token 5: ;
        assertEquals(";", tokens[4].value)
        assertTrue(tokens[4].type is PunctuationType)
    }

    @Test
    fun testPrintlnWithSpaces() {
        val factory = LexerFactoryRegistry.getFactory("1.0")
        val lexer = factory.create()

        val code = "  println  (  \"Hello\"  )  ;  "
        val tokens = lexer.tokenize(code)

        // Los espacios deberían ser ignorados, solo deberíamos tener los tokens importantes
        assertEquals(5, tokens.size)
        assertEquals("println", tokens[0].value)
        assertTrue(tokens[0].type is PrintlnType)
    }

    @Test
    fun testPrintlnWithComments() {
        val factory = LexerFactoryRegistry.getFactory("1.0")
        val lexer = factory.create()

        val code = "println(\"Hello\"); // This is a comment"
        val tokens = lexer.tokenize(code)

        // El comentario debería ser ignorado
        assertEquals(5, tokens.size)
        assertEquals("println", tokens[0].value)
        assertTrue(tokens[0].type is PrintlnType)
    }
}
