package test.lexerTest

import main.kotlin.lexer.LexerFactory
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.io.StringReader

class LexerFactoryTest {
    @Test
    fun testCreateLexerV10() {
        val reader = StringReader("let x = 10;")
        val lexer = LexerFactory.createLexerV10(reader)
        assertNotNull(lexer)
    }

    @Test
    fun testCreateLexerV11() {
        val reader = StringReader("let x = 10;")
        val lexer = LexerFactory.createLexerV11(reader)
        assertNotNull(lexer)
    }

    @Test
    fun testCreateLexerV10WithEmptyString() {
        val reader = StringReader("")
        val lexer = LexerFactory.createLexerV10(reader)
        assertNotNull(lexer)
    }

    @Test
    fun testCreateLexerV11WithEmptyString() {
        val reader = StringReader("")
        val lexer = LexerFactory.createLexerV11(reader)
        assertNotNull(lexer)
    }

    @Test
    fun testCreateLexerV10WithMultipleStatements() {
        val reader = StringReader("let x = 10; let y = 20;")
        val lexer = LexerFactory.createLexerV10(reader)
        assertNotNull(lexer)
    }

    @Test
    fun testCreateLexerV11WithMultipleStatements() {
        val reader = StringReader("let x = 10; const y = 20;")
        val lexer = LexerFactory.createLexerV11(reader)
        assertNotNull(lexer)
    }
}
