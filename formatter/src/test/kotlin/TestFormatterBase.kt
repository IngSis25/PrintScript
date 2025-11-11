package org.example.formatter

import main.kotlin.lexer.LexerFactory
import org.ParserFactory
import java.io.StringReader
import kotlin.test.Test

class TestFormatterBase {
    @Test
    fun testFormatterBaseOutput() {
        val sourceCode = """let something:string = "a really cool thing";"""

        val lexer = LexerFactory.createLexerV10(StringReader(sourceCode))
        val parser = ParserFactory.createParserV10(lexer)
        val formatter = Formatter(parser, sourceCode)

        // Sin reglas - solo el output del FormatterVisitor
        val result = formatter.format(emptyList())
        val formattedCode = result.code

        println("FormatterVisitor output (no rules):")
        println(formattedCode.replace("\n", "\\n"))
    }

    @Test
    fun testFormatterWithNonConfigurableRules() {
        val sourceCode = """let something:string = "a really cool thing";"""

        val lexer = LexerFactory.createLexerV10(StringReader(sourceCode))
        val parser = ParserFactory.createParserV10(lexer)
        val formatter = Formatter(parser, sourceCode)
        val rulesFactory = RulesFactory()

        // Con un JSON vacío - solo reglas no-configurables
        val rules = rulesFactory.getRules("{}", "1.0")
        val result = formatter.format(rules)
        val formattedCode = result.code

        println("With non-configurable rules:")
        println(formattedCode.replace("\n", "\\n"))
    }
}
