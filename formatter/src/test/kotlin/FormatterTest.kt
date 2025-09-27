package org.example.formatter

import com.google.gson.Gson
import factory.LexerFactoryRegistry
import factory.ParserFactoryRegistry
import org.example.formatter.config.FormatterConfig
import java.io.File
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
            let something:string = "a really cool thing";
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

    @Test
    fun onlyLineBreakAfterStatement_preserves_inner_spacing() {
        // En este modo solo queremos insertar '\n' tras ';' y NO tocar los espacios internos
        val json =
            """
            {
              "onlyLineBreakAfterStatement": true
            }
            """.trimIndent()

        // Todo en una sola línea (con distintos espacios alrededor de ':' y '=')
        val main =
            """
            let something:string = "a really cool thing";
            let another_thing: string = "another really cool thing";let twice_thing : string = "another really cool thing twice";let third_thing :string="another really cool thing three times";
            """.trimIndent()

        // Esperado: exactamente igual, SOLO separando por ';'
        val golden =
            """
            let something:string = "a really cool thing";
            let another_thing: string = "another really cool thing";
            let twice_thing : string = "another really cool thing twice";
            let third_thing :string="another really cool thing three times";
            """.trimIndent()

        // Guardar JSON en un archivo temporal porque Formatter.formatSource espera File
        val jsonFile =
            File.createTempFile("formatter", ".json").apply {
                writeText(json)
                deleteOnExit()
            }

        // Formateo por el camino textual
        val actual = Formatter.formatSource(main, jsonFile)

        assertEquals(golden, actual)
    }
}
