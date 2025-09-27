package org.example.formatter

import com.google.gson.Gson
import factory.LexerFactoryRegistry
import factory.ParserFactoryRegistry
import org.example.formatter.config.FormatterConfig
import kotlin.test.Test
import kotlin.test.assertEquals

class FormatterTest {
    @Test
    fun noBlankLinesBetweenPrintln_whenRuleIsZero() {
        val json =
            """
            {
              "lineBreaksBeforePrints": 0
            }
            """.trimIndent()

        val main =
            """
            let something:string = "a really cool thing";
            println(something);
            
            
            println("in the way she moves");
            """.trimIndent()

        val golden =
            """
            let something: string = "a really cool thing";
            println(something);
            println("in the way she moves");
            """.trimIndent()

        // Parse JSON config
        val config = Gson().fromJson(json, FormatterConfig::class.java)

        // Parse PrintScript code
        val lexerFactory = LexerFactoryRegistry.getFactory("1.0")
        val lexer = lexerFactory.create()
        val tokens = lexer.tokenize(main)

        val parserFactory = ParserFactoryRegistry.getFactory("1.0")
        val parser = parserFactory.create()
        val program = parser.parse(tokens)

        // Format the code
        val sb = StringBuilder()
        val visitor = FormatterVisitor(config, sb)
        visitor.evaluateMultiple(program)

        val actual = sb.toString()
        assertEquals(golden, actual)
    }
}
