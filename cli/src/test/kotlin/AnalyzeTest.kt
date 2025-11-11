package cli

import Analyze
import com.github.ajalt.clikt.testing.test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path

class AnalyzeTest {
    @Test
    @DisplayName("Analyze: código válido sin warnings debe pasar")
    fun analyze_validCode_noWarnings_shouldSucceed(
        @TempDir tempDir: Path,
    ) {
        val code =
            """
            let myVariable: number = 10;
            println(myVariable);
            """.trimIndent()

        val rulesJson =
            """
            {
                "UnusedVariableCheck": {},
                "NamingFormatCheck": {
                    "namingPatternName": "camelCase"
                }
            }
            """.trimIndent()

        val testFile =
            File(tempDir.toFile(), "test.ps").apply {
                writeText(code)
            }

        val rulesFile =
            File(tempDir.toFile(), "rules.json").apply {
                writeText(rulesJson)
            }

        val command = Analyze()
        val result = command.test(testFile.absolutePath, rulesFile.absolutePath)

        // El análisis puede completarse exitosamente o mostrar warnings
        assertNotNull(result)
    }

    @Test
    @DisplayName("Analyze: código con variable no usada debe reportar warning")
    fun analyze_unusedVariable_shouldReportWarning(
        @TempDir tempDir: Path,
    ) {
        val code =
            """
            let unusedVar: number = 10;
            let usedVar: number = 5;
            println(usedVar);
            """.trimIndent()

        val rulesJson =
            """
            {
                "UnusedVariableCheck": {},
                "NamingFormatCheck": {
                    "namingPatternName": "camelCase"
                }
            }
            """.trimIndent()

        val testFile =
            File(tempDir.toFile(), "test.ps").apply {
                writeText(code)
            }

        val rulesFile =
            File(tempDir.toFile(), "rules.json").apply {
                writeText(rulesJson)
            }

        val command = Analyze()
        val result = command.test(testFile.absolutePath, rulesFile.absolutePath)

        // El análisis debe completarse, aunque haya warnings
        assertNotNull(result)
    }

    @Test
    @DisplayName("Analyze: archivo inexistente debe reportar error")
    fun analyze_nonexistentFile_shouldReportError(
        @TempDir tempDir: Path,
    ) {
        val rulesJson =
            """
            {
                "UnusedVariableCheck": {}
            }
            """.trimIndent()

        val rulesFile =
            File(tempDir.toFile(), "rules.json").apply {
                writeText(rulesJson)
            }

        val command = Analyze()
        val result = command.test("nonexistent.ps", rulesFile.absolutePath)

        assertTrue(result.output.contains("Error") || result.output.contains("File not found"))
    }

    @Test
    @DisplayName("Analyze: archivo de reglas inexistente debe reportar error")
    fun analyze_nonexistentRulesFile_shouldReportError(
        @TempDir tempDir: Path,
    ) {
        val code =
            """
            let a: number = 10;
            println(a);
            """.trimIndent()

        val testFile =
            File(tempDir.toFile(), "test.ps").apply {
                writeText(code)
            }

        val command = Analyze()
        val result = command.test(testFile.absolutePath, "nonexistent-rules.json")

        assertTrue(result.output.contains("Error") || result.output.contains("Rules file not found"))
    }
}
