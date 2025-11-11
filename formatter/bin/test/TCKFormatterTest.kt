package org.example.formatter

import main.kotlin.lexer.LexerFactory
import org.ParserFactory
import java.io.File
import java.io.StringReader
import kotlin.test.Test
import kotlin.test.assertEquals

class TCKFormatterTest {
    @Test
    fun testAllTCKCases_V10() {
        val testDir = File("src/test/resources/tck-tests/1.0")
        val testCases = testDir.listFiles()?.filter { it.isDirectory } ?: emptyList()

        println("\n========== TCK Tests V1.0 ==========")
        var passed = 0
        var failed = 0

        for (testCase in testCases.sortedBy { it.name }) {
            val mainFile = File(testCase, "main.ps")
            val goldenFile = File(testCase, "golden.ps")
            val configFile = File(testCase, "config.json")

            if (!mainFile.exists() || !goldenFile.exists() || !configFile.exists()) {
                println("⚠️  ${testCase.name}: SKIP (missing files)")
                continue
            }

            val sourceCode = mainFile.readText()
            val expectedOutput = goldenFile.readText()
            val configJson = configFile.readText()
            var formattedCode = ""

            try {
                val lexer = LexerFactory.createLexerV10(StringReader(sourceCode))
                val parser = ParserFactory.createParserV10(lexer)
                val formatter = Formatter(parser, sourceCode)
                val rulesFactory = RulesFactory()
                val rules = rulesFactory.getRules(configJson, "1.0")
                val result = formatter.format(rules)
                formattedCode = result.code

                // Eliminar saltos de línea al final
                while (formattedCode.endsWith("\n")) {
                    formattedCode = formattedCode.substring(0, formattedCode.length - 1)
                }

                assertEquals(expectedOutput.trimEnd(), formattedCode, "Test case: ${testCase.name}")
                println("✅ ${testCase.name}")
                passed++
            } catch (e: AssertionError) {
                println("❌ ${testCase.name}")
                println("   Expected: ${expectedOutput.trimEnd().replace("\n", "\\n")}")
                println("   Got:      ${formattedCode.replace("\n", "\\n")}")
                failed++
            } catch (e: Exception) {
                println("💥 ${testCase.name}: ${e.message}")
                failed++
            }
        }

        println("\n========== Results V1.0 ==========")
        println("✅ Passed: $passed")
        println("❌ Failed: $failed")
        println("Total: ${passed + failed}")
        println("==================================\n")

        if (failed > 0) {
            throw AssertionError("$failed test(s) failed in V1.0")
        }
    }

    @Test
    fun testAllTCKCases_V11() {
        val testDir = File("src/test/resources/tck-tests/1.1")
        val testCases = testDir.listFiles()?.filter { it.isDirectory } ?: emptyList()

        println("\n========== TCK Tests V1.1 ==========")
        var passed = 0
        var failed = 0

        for (testCase in testCases.sortedBy { it.name }) {
            val mainFile = File(testCase, "main.ps")
            val goldenFile = File(testCase, "golden.ps")
            val configFile = File(testCase, "config.json")

            if (!mainFile.exists() || !goldenFile.exists() || !configFile.exists()) {
                println("⚠️  ${testCase.name}: SKIP (missing files)")
                continue
            }

            val sourceCode = mainFile.readText()
            val expectedOutput = goldenFile.readText()
            val configJson = configFile.readText()
            var formattedCode = ""

            try {
                val lexer = LexerFactory.createLexerV11(StringReader(sourceCode))
                val parser = ParserFactory.createParserV11(lexer)
                val formatter = Formatter(parser, sourceCode)
                val rulesFactory = RulesFactory()
                val rules = rulesFactory.getRules(configJson, "1.1")
                val result = formatter.format(rules)
                formattedCode = result.code

                // Eliminar saltos de línea al final
                while (formattedCode.endsWith("\n")) {
                    formattedCode = formattedCode.substring(0, formattedCode.length - 1)
                }

                assertEquals(expectedOutput.trimEnd(), formattedCode, "Test case: ${testCase.name}")
                println("✅ ${testCase.name}")
                passed++
            } catch (e: AssertionError) {
                println("❌ ${testCase.name}")
                println("   Expected: ${expectedOutput.trimEnd().replace("\n", "\\n")}")
                println("   Got:      ${formattedCode.replace("\n", "\\n")}")
                failed++
            } catch (e: Exception) {
                println("💥 ${testCase.name}: ${e.message}")
                failed++
            }
        }

        println("\n========== Results V1.1 ==========")
        println("✅ Passed: $passed")
        println("❌ Failed: $failed")
        println("Total: ${passed + failed}")
        println("==================================\n")

        if (failed > 0) {
            throw AssertionError("$failed test(s) failed in V1.1")
        }
    }
}
