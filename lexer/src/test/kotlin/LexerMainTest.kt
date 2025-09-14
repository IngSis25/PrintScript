package main.kotlin.lexer

import factory.LexerFactoryRegistry
import org.junit.jupiter.api.Test
import types.PrintlnType

class LexerMainTest {
    @Test
    fun testPrintlnToken() {
        // Probamos que el lexer reconoce println como PrintlnType
        val factory = LexerFactoryRegistry.getFactory("1.0")
        val lexer = factory.create()

        val code = "println(\"This is a text\");"
        val tokens = lexer.tokenize(code)

        // Verificamos que se generaron los tokens esperados
        assert(tokens.isNotEmpty())

        // Buscamos el token println
        val printlnToken = tokens.find { it.value == "println" }
        assert(printlnToken != null)
        assert(printlnToken!!.type is PrintlnType)

        // Verificamos que hay tokens para el string
        val stringToken = tokens.find { it.value == "\"This is a text\"" }
        assert(stringToken != null)
    }
}
