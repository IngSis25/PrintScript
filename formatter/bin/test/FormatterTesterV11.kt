package org.example.formatter

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import main.kotlin.lexer.LexerFactory
import org.Parser
import org.ParserFactory
import org.example.formatter.Formatter
import org.example.formatter.config.FormatterConfig
import java.io.File
import java.io.StringReader
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class FormatterTesterV11 {
    private fun getJsonFromFile(): JsonObject {
        val jsonContent = File("src/test/resources/rulesExample.json").readText()
        return JsonParser.parseString(jsonContent).asJsonObject
    }

    private fun createFormatterConfig(json: JsonObject): FormatterConfig {
        // Mapear claves antiguas a nuevas si es necesario
        val normalizedJson = JsonParser.parseString(json.toString()).asJsonObject

        // Mapear claves antiguas a nuevas
        val mappedJson = com.google.gson.JsonObject()

        // Mapear space_before_colon -> enforce-spacing-before-colon-in-declaration
        json.get("space_before_colon")?.let { mappedJson.add("enforce-spacing-before-colon-in-declaration", it) }

        // Mapear space_after_colon -> enforce-spacing-after-colon-in-declaration
        json.get("space_after_colon")?.let { mappedJson.add("enforce-spacing-after-colon-in-declaration", it) }

        // Mapear space_around_equals -> enforce-spacing-around-equals
        json.get("space_around_equals")?.let { mappedJson.add("enforce-spacing-around-equals", it) }

        // Mapear newline_after_println -> line-breaks-after-println
        json.get("newline_after_println")?.let { mappedJson.add("line-breaks-after-println", it) }

        // Mapear newline_before_println -> line-breaks-after-println (si existe)
        json.get("newline_before_println")?.let { mappedJson.add("line-breaks-after-println", it) }

        // Mapear if_brace_same_line -> if-brace-same-line
        json.get("if_brace_same_line")?.let { mappedJson.add("if-brace-same-line", it) }

        // Mapear same_line_for_if_brace -> if-brace-same-line
        json.get("same_line_for_if_brace")?.let { mappedJson.add("if-brace-same-line", it) }

        // Mapear indent_inside_if -> indent-inside-if
        json.get("indent_inside_if")?.let { mappedJson.add("indent-inside-if", it) }

        // Mapear number_of_spaces_indentation -> indent-inside-if
        json.get("number_of_spaces_indentation")?.let { mappedJson.add("indent-inside-if", it) }

        // Si no hay mapeos, usar el JSON original
        val finalJson = if (mappedJson.size() > 0) mappedJson else json

        val gson = Gson()
        return gson.fromJson(finalJson, FormatterConfig::class.java)
    }

    private fun compareResults(
        formatter: Formatter,
        parser: Parser,
        shouldSucceed: Boolean,
        file: File,
        solution: List<String>,
    ) {
        try {
            val nodes = parser.collectAllASTNodes()
            val result = formatter.format(nodes.iterator()).split("\n").filter { it.isNotEmpty() }
            if (!shouldSucceed) {
                assertFalse(true, "Expected an error but test passed for file ${file.name}")
            }

            for (i in solution.indices) {
                assertEquals(
                    solution[i],
                    result[i],
                    "Mismatch in file \"${file.name}\" at line ${i + 1}",
                )
            }
        } catch (e: Exception) {
            if (shouldSucceed) {
                assertFalse(true, "Unexpected error in file ${file.name}: ${e.message}")
            }
        }
    }

    @Test
    fun testFiles() {
        val examplesDir = File("src/test/resources/examples-v11")
        val reader = TestReader()

        examplesDir.listFiles { file -> file.isFile && file.extension == "txt" }?.forEach { file ->
            val (code, solution, shouldSucceed, json) = reader.readTokens(file.path)
            val lexer = LexerFactory.createLexerV11(StringReader(code))
            val parser = ParserFactory.createParserV11(lexer)

            val config = createFormatterConfig(json)
            val formatter = Formatter(config)
            compareResults(formatter, parser, shouldSucceed, file, solution)
        }
    }

    @Test
    fun testSingleFile() {
        val file = File("src/test/resources/examples-v11/ifelsestatement.txt")

        val reader = TestReader()
        val (code, _, _, json) = reader.readTokens(file.path)

        val lexer = LexerFactory.createLexerV11(StringReader(code))
        val parser = ParserFactory.createParserV11(lexer)

        val config = createFormatterConfig(json)
        val formatter = Formatter(config)
        val nodes = parser.collectAllASTNodes()
        println(formatter.format(nodes.iterator()))
    }

    @Test
    fun testFormat() {
        val input = "let a: number = 10;"
        val lexer = LexerFactory.createLexerV11(StringReader(input))
        val parser = ParserFactory.createParserV11(lexer)

        // Get JSON from file
        val json = getJsonFromFile()
        val config = createFormatterConfig(json)
        val formatter = Formatter(config)
        val nodes = parser.collectAllASTNodes()
        println(formatter.format(nodes.iterator()))
    }

    @Test
    fun testIfElseStatement() {
        val input = "if(true){let a:number=5;}else{let b:number=10;}"

        val lexer = LexerFactory.createLexerV11(StringReader(input))
        val parser = ParserFactory.createParserV11(lexer)

        // Get JSON from file
        val json = getJsonFromFile()
        val config = createFormatterConfig(json)
        val formatter = Formatter(config)
        val nodes = parser.collectAllASTNodes()
        println("Nodes count: ${nodes.size}")
        nodes.forEach { println("Node type: ${it::class.simpleName}") }
        println("Formatted output:")
        println(formatter.format(nodes.iterator()))
    }

    @Test
    fun testNestedIf() {
        val input = "if(true){if(true){if(true){println('Hello');}}}"

        val lexer = LexerFactory.createLexerV11(StringReader(input))
        val parser = ParserFactory.createParserV11(lexer)

        val json = com.google.gson.JsonObject()
        json.addProperty("number_of_spaces_indentation", 4)
        json.addProperty("same_line_for_if_brace", true)

        val config = createFormatterConfig(json)
        val formatter = Formatter(config)
        val nodes = parser.collectAllASTNodes()
        val result = formatter.format(nodes.iterator())
        println("Result:")
        println(result)
        println("\nLine by line:")
        result.split("\n").forEachIndexed { index, line ->
            println(
                "Line ${index + 1}: '$line' (${line.length} chars, ${line.takeWhile {
                    it == ' '
                }.length} leading spaces)",
            )
        }
    }

    @Test
    fun testReadInput() {
        val input = "let a: string = readInput('Enter your name:');"

        val lexer = LexerFactory.createLexerV11(StringReader(input))
        val parser = ParserFactory.createParserV11(lexer)

        val json = getJsonFromFile()
        val config = createFormatterConfig(json)
        val formatter = Formatter(config)
        val nodes = parser.collectAllASTNodes()
        println(formatter.format(nodes.iterator()))
    }

    @Test
    fun testReadEnv() {
        val input = "let path: string = readEnv('PATH');"

        val lexer = LexerFactory.createLexerV11(StringReader(input))
        val parser = ParserFactory.createParserV11(lexer)

        val json = getJsonFromFile()
        val config = createFormatterConfig(json)
        val formatter = Formatter(config)
        val nodes = parser.collectAllASTNodes()
        println(formatter.format(nodes.iterator()))
    }
}
