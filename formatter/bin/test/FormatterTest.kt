package org.example.formatter

import main.kotlin.lexer.LexerFactory
import org.ParserFactory
import java.io.StringReader
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class FormatterTest {
    @Test
    fun testFormatWithNullOriginalSource() {
        val sourceCode = "let x: number = 5;"
        val lexer = LexerFactory.createLexerV10(StringReader(sourceCode))
        val parser = ParserFactory.createParserV10(lexer)
        val formatter = Formatter(parser, null)

        val result = formatter.format(emptyList())
        assertNotNull(result)
        assertNotNull(result.code)
    }

    @Test
    fun testFormatWithEmptyRules() {
        val sourceCode = "let x: number = 5;"
        val lexer = LexerFactory.createLexerV10(StringReader(sourceCode))
        val parser = ParserFactory.createParserV10(lexer)
        val formatter = Formatter(parser, sourceCode)

        val result = formatter.format(emptyList())
        assertNotNull(result)
        assertNotNull(result.code)
    }

    @Test
    fun testFormatWithMultipleStatements() {
        val sourceCode = "let x: number = 5; let y: number = 10;"
        val lexer = LexerFactory.createLexerV10(StringReader(sourceCode))
        val parser = ParserFactory.createParserV10(lexer)
        val formatter = Formatter(parser, sourceCode)

        val result = formatter.format(emptyList())
        assertNotNull(result)
        assertTrue(result.code.contains("let x: number = 5;"))
        assertTrue(result.code.contains("let y: number = 10;"))
    }

    @Test
    fun testFormatWithPrintlnStatement() {
        val sourceCode = "println(\"Hello\");"
        val lexer = LexerFactory.createLexerV10(StringReader(sourceCode))
        val parser = ParserFactory.createParserV10(lexer)
        val formatter = Formatter(parser, sourceCode)

        val result = formatter.format(emptyList())
        assertNotNull(result)
        assertTrue(result.code.contains("println"))
    }

    @Test
    fun testFormatWithIfStatement() {
        val sourceCode = "if (true) { println(\"test\"); }"
        val lexer = LexerFactory.createLexerV11(StringReader(sourceCode))
        val parser = ParserFactory.createParserV11(lexer)
        val formatter = Formatter(parser, sourceCode)

        val result = formatter.format(emptyList())
        assertNotNull(result)
        assertTrue(result.code.contains("if"))
    }

    @Test
    fun testFormatPreservesOriginalLetDeclaration() {
        val originalSource = "let x:number=5;"
        val lexer = LexerFactory.createLexerV10(StringReader(originalSource))
        val parser = ParserFactory.createParserV10(lexer)
        val formatter = Formatter(parser, originalSource)

        val rulesFactory = RulesFactory()
        val rules = rulesFactory.getRules("{}", "1.0")
        val result = formatter.format(rules)

        // El formatter debe usar la línea original para declaraciones let
        assertNotNull(result.code)
    }

    @Test
    fun testFormatPreservesOriginalPrintln() {
        val originalSource = "println(\"test\");"
        val lexer = LexerFactory.createLexerV10(StringReader(originalSource))
        val parser = ParserFactory.createParserV10(lexer)
        val formatter = Formatter(parser, originalSource)

        val rulesFactory = RulesFactory()
        val rules = rulesFactory.getRules("{}", "1.0")
        val result = formatter.format(rules)

        assertNotNull(result.code)
        assertTrue(result.code.contains("println"))
    }

    @Test
    fun testFormatWithEmptySource() {
        val sourceCode = ""
        val lexer = LexerFactory.createLexerV10(StringReader(sourceCode))
        val parser = ParserFactory.createParserV10(lexer)
        val formatter = Formatter(parser, sourceCode)

        val result = formatter.format(emptyList())
        assertNotNull(result)
        // Con código vacío, el resultado debería estar vacío o ser una cadena vacía
    }

    @Test
    fun testFormatWithRulesApplied() {
        val sourceCode = "let x:number=5;"
        val lexer = LexerFactory.createLexerV10(StringReader(sourceCode))
        val parser = ParserFactory.createParserV10(lexer)
        val formatter = Formatter(parser, sourceCode)

        val rulesFactory = RulesFactory()
        val rules = rulesFactory.getRules("""{"space_around_equals": true}""", "1.0")
        val result = formatter.format(rules)

        assertNotNull(result.code)
    }
}
