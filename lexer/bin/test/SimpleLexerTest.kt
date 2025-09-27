package main.kotlin.lexer

import factory.LexerFactoryRegistry
import org.junit.jupiter.api.Test
import types.PrintlnType
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SimpleLexerTest {
    @Test
    fun testPrintlnBasic() {
        val factory = LexerFactoryRegistry.getFactory("1.0")
        val lexer = factory.create()

        val code = "println(\"Hello\");"
        val tokens = lexer.tokenize(code)

        // Deberíamos tener 5 tokens: println, (, "Hello", ), ;
        assertEquals(5, tokens.size)

        // Verificar que println es reconocido como PrintlnType
        val printlnToken = tokens.find { it.value == "println" }
        assertTrue(printlnToken != null)
        assertTrue(printlnToken!!.type is PrintlnType)

        // Verificar que el string es reconocido
        val stringToken = tokens.find { it.value == "\"Hello\"" }
        assertTrue(stringToken != null)
    }
}
