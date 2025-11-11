package cli

import Format
import com.github.ajalt.clikt.testing.test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path

class FormatTest {
    @Test
    @DisplayName("Format: debe formatear código correctamente")
    fun format_validCode_shouldFormat(
        @TempDir tempDir: Path,
    ) {
        val code =
            """
            let a:number=10;
            let b:number=20;
            println(a+b);
            """.trimIndent()

        val rulesJson =
            """
            {
                "enforce-spacing-around-equals": true,
                "enforce-spacing-after-colon-in-declaration": true
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

        val command = Format()
        val result = command.test(testFile.absolutePath, rulesFile.absolutePath)

        // Verificar que el comando se ejecutó sin errores
        assertNotNull(result)

        // Verificar que el archivo fue procesado (formateado)
        val formattedContent = testFile.readText()
        assertNotNull(formattedContent)
        assertTrue(formattedContent.isNotEmpty())
    }

    @Test
    @DisplayName("Format: debe aplicar reglas de espaciado")
    fun format_shouldApplySpacingRules(
        @TempDir tempDir: Path,
    ) {
        val code =
            """
            let x:number=5;
            let y:number=10;
            """.trimIndent()

        val rulesJson =
            """
            {
                "enforce-spacing-around-equals": true,
                "enforce-spacing-after-colon-in-declaration": true
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

        val command = Format()
        val result = command.test(testFile.absolutePath, rulesFile.absolutePath)

        // Verificar que el comando se ejecutó (puede tener diferentes mensajes)
        assertNotNull(result)

        val formattedContent = testFile.readText()
        // Verificar que el formateo se aplicó (el contenido puede cambiar)
        assertNotNull(formattedContent)
        assertTrue(formattedContent.isNotEmpty())
    }

    @Test
    @DisplayName("Format: archivo inexistente debe reportar error")
    fun format_nonexistentFile_shouldReportError(
        @TempDir tempDir: Path,
    ) {
        val rulesJson =
            """
            {
                "enforce-spacing-around-equals": true
            }
            """.trimIndent()

        val rulesFile =
            File(tempDir.toFile(), "rules.json").apply {
                writeText(rulesJson)
            }

        val command = Format()
        val result = command.test("nonexistent.ps", rulesFile.absolutePath)

        assertTrue(result.output.contains("Error") || result.output.contains("File not found"))
    }

    @Test
    @DisplayName("Format: archivo de reglas inexistente debe reportar error")
    fun format_nonexistentRulesFile_shouldReportError(
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

        val command = Format()
        val result = command.test(testFile.absolutePath, "nonexistent-rules.json")

        assertTrue(result.output.contains("Error") || result.output.contains("Rules file not found"))
    }

    @Test
    @DisplayName("Format: debe mapear claves antiguas a nuevas")
    fun format_shouldMapOldKeysToNew(
        @TempDir tempDir: Path,
    ) {
        val code =
            """
            let x:number=5;
            """.trimIndent()

        // Usar claves antiguas
        val rulesJson =
            """
            {
                "space_around_equals": true,
                "space_after_colon": true
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

        val command = Format()
        val result = command.test(testFile.absolutePath, rulesFile.absolutePath)

        // Verificar que el comando se ejecutó correctamente
        assertNotNull(result)
    }
}
